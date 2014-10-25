/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import ABMs.ABMSocios;
import BD.ConexionBD;
import Interfaces.AsistenciasGui;
import Interfaces.IngresoGui;
import Interfaces.RegistrarPagoGui;
import Interfaces.asistenciaCombo;
import Interfaces.busquedaManualGui;
import Modelos.Arancel;
import Modelos.Asistencia;
import Modelos.Socio;
import Modelos.Socioarancel;
import com.digitalpersona.onetouch.DPFPDataPurpose;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPFingerIndex;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.DPFPSample;
import com.digitalpersona.onetouch.DPFPTemplate;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.DPFPDataAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPDataEvent;
import com.digitalpersona.onetouch.capture.event.DPFPErrorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPErrorEvent;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPReaderStatusEvent;
import com.digitalpersona.onetouch.capture.event.DPFPSensorAdapter;
import com.digitalpersona.onetouch.capture.event.DPFPSensorEvent;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

/**
 *
 * @author Nico
 */
public class ControladorIngreso implements ActionListener {
//Varible que permite iniciar el dispositivo de lector de huella conectado
// con sus distintos metodos.

    public static DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();
//Varible que permite establecer las capturas de la huellas, para determina sus caracteristicas
// y poder estimar la creacion de un template de la huella para luego poder guardarla
    private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();
//Esta variable tambien captura una huella del lector y crea sus caracteristcas para auntetificarla
// o verificarla con alguna guardada en la BD
    private DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();
//Variable que para crear el template de la huella luego de que se hallan creado las caracteriticas
// necesarias de la huella si no ha ocurrido ningun problema
    private DPFPTemplate template;
    public static String TEMPLATE_PROPERTY = "template";
    public DPFPFeatureSet featuresinscripcion;
    public DPFPFeatureSet featuresverificacion;
    private IngresoGui ingresoGui;
    ConexionBD con = new ConexionBD();
    private DPFPTemplate templateIndividual;
    private DPFPFingerIndex fingerIndividual;
    private int idCliente;
    private ABMSocios abmSocio;
    private Socio socio;
    private Asistencia asistencia;
    private Timer timer;

    public ControladorIngreso(IngresoGui ingresoGui) throws Exception {
        this.ingresoGui = ingresoGui;
        ingresoGui.limpiar();
        ingresoGui.setActionListener(this);
        Iniciar();
        start();
        abmSocio = new ABMSocios();
        asistencia = new Asistencia();
        timer= new Timer(7000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                limpiaPantalla();
                timer.stop();
            }
        });
        timer.start();
    }

    private void limpiaPantalla(){
        ingresoGui.limpiar();
    }
    public void abrirBase() {
        if (!Base.hasConnection()) {
            Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/GYM", "root", "root");
        }
    }

    /*va true si se quiere usar para mostrarla por pantalla es decir 12/12/2014 y false si va 
     para la base de datos, es decir 2014/12/12*/
    public String dateToMySQLDate(Date fecha, boolean paraMostrar) {
        if (paraMostrar) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(fecha);
        } else {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(fecha);
        }
    }

    protected void Iniciar() {
        Lector.addDataListener(new DPFPDataAdapter() {
            @Override
            public void dataAcquired(final DPFPDataEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnviarTexto("La Huella Digital ha sido Capturada");
                        try {
                            ProcesarCaptura(e.getSample());
                        } catch (IOException ex) {
                            Logger.getLogger(ControladorIngreso.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
            }
        });

        Lector.addReaderStatusListener(new DPFPReaderStatusAdapter() {
            @Override
            public void readerConnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnviarTexto("El Sensor de Huella Digital esta Activado o Conectado");
                    }
                });
            }

            @Override
            public void readerDisconnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnviarTexto("El Sensor de Huella Digital esta Desactivado o no Conectado");
                    }
                });
            }
        });

        Lector.addSensorListener(new DPFPSensorAdapter() {
            @Override
            public void fingerTouched(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnviarTexto("El dedo ha sido colocado sobre el Lector de Huella");
                        ingresoGui.setVisible(true);
                        ingresoGui.toFront();
                        ingresoGui.requestFocus();
                        //Aca debe abrirse la ventana cuando no está minimiza

                    }
                });
            }

            @Override
            public void fingerGone(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnviarTexto("El dedo ha sido quitado del Lector de Huella");
                    }
                });
            }
        });

        Lector.addErrorListener(new DPFPErrorAdapter() {
            public void errorReader(final DPFPErrorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnviarTexto("Error: " + e.getError());
                    }
                });
            }
        });
    }

    public void ProcesarCaptura(DPFPSample sample) throws IOException {
        // Procesar la muestra de la huella y crear un conjunto de características con el propósito de inscripción.
        featuresinscripcion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

        // Procesar la muestra de la huella y crear un conjunto de características con el propósito de verificacion.
        featuresverificacion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

        // Comprobar la calidad de la muestra de la huella y lo añade a su reclutador si es bueno
        if (featuresinscripcion != null) {
            // try {
            System.out.println("Las Caracteristicas de la Huella han sido creada");
            //Reclutador.addFeatures(featuresinscripcion);// Agregar las caracteristicas de la huella a la plantilla a crear
            identificarHuella();
            // Dibuja la huella dactilar capturada.
            Image image = CrearImagenHuella(sample);
            DibujarHuella(image);

            //} catch (DPFPImageQualityException ex) {
            //   System.err.println("Error: " + ex.getMessage());
            //} //finally {
            // Comprueba si la plantilla se ha creado.
                /*switch (Reclutador.getTemplateStatus()) {
             case TEMPLATE_STATUS_READY:	// informe de éxito y detiene  la captura de huellas
             stop();
             setTemplate(Reclutador.getTemplate());
             EnviarTexto("La Plantilla de la Huella ha Sido Creada, ya puede Verificarla o Identificarla");

             break;

             case TEMPLATE_STATUS_FAILED: // informe de fallas y reiniciar la captura de huellas
             Reclutador.clear();
             stop();
             setTemplate(null);
             EnviarTexto("La Plantilla de la Huella no pudo ser creada, Repita el Proceso");
             start();
             break;
             }*/
            //}
        }
    }

    public DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose) {
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try {
            return extractor.createFeatureSet(sample, purpose);
        } catch (DPFPImageQualityException e) {
            return null;
        }
    }

    public void EnviarTexto(String string) {
        ingresoGui.getTextoHuella().append(string + "\n");
        ingresoGui.getTextoHuella().setCaretPosition(ingresoGui.getTextoHuella().getDocument().getLength());

    }

    public void start() {
        Lector.startCapture();
        EnviarTexto("Utilizando el Lector de Huella Dactilar ");
    }

    public void stop() {
        Lector.stopCapture();
        EnviarTexto("No se está usando el Lector de Huella Dactilar ");
    }

    public DPFPTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DPFPTemplate template) {
        this.template = template;
    }

    public void identificarHuella() throws IOException {
        try {
            Connection c = con.conectar();
            String query = "SELECT id,huella,dedo, client_id FROM huellas h inner join socios s on h.client_id= s.ID_DATOS_PERS  where  s.ACTIVO = 1";
            PreparedStatement stmt = c.prepareStatement(query);
            //stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery(query);
            //Ejecuta la sentencia
            boolean encontro=false;
            while (rs.next() && !encontro) {
                fingerIndividual = DPFPFingerIndex.valueOf(rs.getString(3));
                byte templateBuffer[] = rs.getBytes(2);
                idCliente = rs.getInt(4);
                System.out.println("entre");
                //Crea una nueva plantilla a partir de la guardada en la base de datos
                templateIndividual = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
                //Envia la plantilla creada al objeto contendor de Template del componente de huella digital
                setTemplate(templateIndividual);

                // Compara las caracteriticas de la huella recientemente capturda con la
                // alguna plantilla guardada en la base de datos que coincide con ese tipo
                DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());

                //compara las plantilas (actual vs bd)
                //Si encuentra correspondencia dibuja el mapa
                //e indica el nombre de la persona que coincidió.
                if (result.isVerified()) {//ACA DEBE IR RESULT.ISveRIFIED() ESTOY PROBANDO
                    //crea la imagen de los datos guardado de las huellas guardadas en la base de datos
                    socio = Socio.findFirst("ID_DATOS_PERS = ?", idCliente);
                    EnviarTexto("Verificación correcta,la huella capturada es de " + socio.getString("NOMBRE") + " " + socio.getString("APELLIDO"));
                    cargarDatos(socio);
                    encontro=true;
                    return;
                }

            }
            //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje
            try {
                cargarSonido("error.wav");
                System.out.println("no te conozco wacho");
                
                
            } catch (Exception ex) {
                Logger.getLogger(ControladorIngreso.class.getName()).log(Level.SEVERE, null, ex);
            }
            EnviarTexto("No existe ningún registro que coincida con la huella");
            setTemplate(null);
            ingresoGui.noIdentifado();
            timer.start();
        } catch (SQLException e) {
            //Si ocurre un error lo indica en la consola
            System.err.println("Error al identificar huella dactilar." + e.getMessage());
        } finally {
            con.desconectar();
        }
    }

    public Image CrearImagenHuella(DPFPSample sample) {
        return DPFPGlobal.getSampleConversionFactory().createImage(sample);
    }

    public void DibujarHuella(Image image) {
        ingresoGui.getImHuella().setIcon(new ImageIcon(
                image.getScaledInstance(ingresoGui.getImHuella().getWidth(), ingresoGui.getImHuella().getHeight(), Image.SCALE_DEFAULT)));
        ingresoGui.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ingresoGui.getLimpiarVentana()) {
            System.out.println("limpiar ventana");
            ingresoGui.limpiar();
        }
        if (e.getSource() == ingresoGui.getBusquedaManual()) {
            System.out.println("busqueda manual");
            busquedaManualGui bus = new busquedaManualGui(ingresoGui, true, this);
            bus.setLocationRelativeTo(null);
            bus.setVisible(true);
        }
        if (e.getSource() == ingresoGui.getDarDeAlta()) {
            System.out.println("dar de alta");
            Base.openTransaction();
            RegistrarPagoGui pagoGui = new RegistrarPagoGui(ingresoGui, true, socio);
            pagoGui.setLocationRelativeTo(null);
            pagoGui.setVisible(true);
        }
        if(e.getSource()==ingresoGui.getGestAsis() ){
            AsistenciasGui asistenciasGui= new AsistenciasGui(ingresoGui, true, socio.getInteger("ID_DATOS_PERS"));
            asistenciasGui.setLocationRelativeTo(null);
            asistenciasGui.setVisible(true);
            ingresoGui.limpiarAsistencias();
            cargarAsistencia();
        }
    }

    private void cargarAsistencia() {
        Component[] array = ingresoGui.getPanelAsistencia().getComponents();
        socio = Socio.findFirst("ID_DATOS_PERS = ?", idCliente);
        LazyList<Asistencia> asistencias = Asistencia.where("ID_DATOS_PERS = ? and FECHA >= ?", idCliente, socio.getDate("FECHA_ULT_PAGO"));
        asistencias.orderBy("ID_ASISTENCIA");
        Iterator<Asistencia> it = asistencias.iterator();
        int i = 0;
        int j;
        while (it.hasNext() &&i<30) {
            Asistencia asis= it.next();
            Arancel ar= Arancel.findFirst("id = ?", asis.get("ID_ACTIV"));
            String nombreActiv= ar.getString("nombre");
            String nombreActivCombo="";
            if(asis.get("ID_ACTIV_COMBO")!=null){
                ar= Arancel.findFirst("id = ?", asis.get("ID_ACTIV_COMBO"));
                nombreActivCombo=ar.getString("nombre");
            }
            j=0;
            boolean mismaFecha=false;
            while(j<30&&!mismaFecha){
                if(((JTextArea)((JViewport)((JScrollPane) array[j]).getComponent(0)).getView()).getText().contains(dateToMySQLDate(asis.getDate("FECHA"), true))){
                    mismaFecha=true;
                    ((JTextArea)((JViewport)((JScrollPane) array[j]).getComponent(0)).getView()).append("\n-----------\n"+nombreActiv+" - "+nombreActivCombo);
                }
                j++;
        }
            if(!mismaFecha){
            ((JTextArea)((JViewport)((JScrollPane) array[i]).getComponent(0)).getView()).setText(dateToMySQLDate(asis.getDate("FECHA"), true)+" - \n"+nombreActiv +" - "+nombreActivCombo);
            i++;
            }

        }

    }

    public void cargarDatos(Socio socio) {
        this.socio= socio;
        ingresoGui.limpiar();
        timer.stop();
        idCliente = socio.getInteger("ID_DATOS_PERS");

        ingresoGui.getNombre().setText(socio.getString("APELLIDO")+" "+socio.getString("NOMBRE"));
        if (socio.getDate("FECHA_ULT_PAGO") != null) {
            ingresoGui.getFechaUltPago().setText(dateToMySQLDate(socio.getDate("FECHA_ULT_PAGO"), true));
        } else {
            
            ingresoGui.getFechaUltPago().setText("No hay registro de pago");
        }
        if (socio.getDate("FECHA_PROX_PAGO") != null) {
            ingresoGui.getFechaVence().setText(dateToMySQLDate(socio.getDate(("FECHA_PROX_PAGO")), true));

            // calcular la diferencia en dias
            // Crear 2 instancias de Calendar
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(socio.getDate("FECHA_PROX_PAGO"));
            // calcular la diferencia en milisengundos
            // conseguir la representacion de la fecha en milisegundos
            long milis1 = cal1.getTimeInMillis();
            long milis2 = cal2.getTimeInMillis();
            long diff = milis2 - milis1;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            System.out.println(diffDays);
            ingresoGui.getCantDias().setText(String.valueOf(diffDays));

            try {
                if (Long.valueOf(ingresoGui.getCantDias().getText()) < 0) {
                    System.out.println("vencido! :O");
                    cargarSonido("vencido.wav");
                    ingresoGui.getFechaVence().setText(ingresoGui.getFechaVence().getText().concat(" ¡VENCIDO!"));
                } else {
                    cargarSonido("correcto.wav");
                }
                if (socio.getBoolean("ACTIVO")) {
                    LazyList<Socioarancel> socioArancel= Socioarancel.where("id_socio = ?", idCliente);
                    boolean comboSolo=false;
                    if(socioArancel.size()==1){
                         Arancel aran = Arancel.findFirst("id = ?", socioArancel.get(0).get("id_arancel"));
                        if(aran.getString("categoria").equals("COMBO"))
                            comboSolo=true;
                        else {
                            Calendar calendario = Calendar.getInstance();
                            calendario.add(Calendar.DATE, -calendario.get(Calendar.DAY_OF_WEEK) + 1);
                            LazyList<Asistencia> asi = Asistencia.where("ID_DATOS_PERS = ? and FECHA> ? and ID_ACTIV =?", idCliente, dateToMySQLDate(calendario.getTime(), false), aran.get("id"));
                            int contador = asi.size();
                            if ((contador < aran.getInteger("dias"))&&contador>=0) {
                                Base.openTransaction();
                                Asistencia.createIt("ID_DATOS_PERS", idCliente, "FECHA", dateToMySQLDate(Calendar.getInstance().getTime(), false),"ID_ACTIV",aran.get("id"));
                                Base.commitTransaction();
                            }
                            else{
                                JOptionPane.showMessageDialog(ingresoGui, "Ya asistió el máximo permitido por semana ("+aran.getInteger("dias")+" veces por semana)", "¡Limite!", JOptionPane.INFORMATION_MESSAGE);
                            }
                            
                        }
                    }
                    if(socioArancel.size()>1 || comboSolo){
                        asistenciaCombo asisComboGui= new asistenciaCombo(ingresoGui, true, socioArancel,idCliente);
                        asisComboGui.setLocationRelativeTo(null);
                        asisComboGui.setVisible(true);
                        int idActiv=asisComboGui.idActiv;
                        int idActivCombo= asisComboGui.idActivCombo;
                        asisComboGui.dispose();
                        if(idActiv!=-1){
                            if(idActivCombo!=-1){
                                Base.openTransaction();
                                Asistencia.createIt("ID_DATOS_PERS", idCliente, "FECHA", dateToMySQLDate(Calendar.getInstance().getTime(), false),"ID_ACTIV",idActiv,"ID_ACTIV_COMBO",idActivCombo);
                                Base.commitTransaction();
                            }
                            else{
                                Base.openTransaction();
                                Asistencia.createIt("ID_DATOS_PERS", idCliente, "FECHA", dateToMySQLDate(Calendar.getInstance().getTime(), false),"ID_ACTIV",idActiv);
                                Base.commitTransaction();
                            }
                        }
                    }
   
                    cargarAsistencia();
                }
                else{
                    JOptionPane.showMessageDialog(ingresoGui, "El socio no se encuentra activo, realice un pago para activarlo", "Socio inactivo", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                Logger.getLogger(ControladorIngreso.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            ingresoGui.getFechaVence().setText("No hay registro de vencimiento");
        }

        ingresoGui.getDarDeAlta().setEnabled(!socio.getBoolean("ACTIVO"));
        ingresoGui.getGestAsis().setEnabled(socio.getBoolean("ACTIVO"));
        timer.start();

    }

    public void cargarSonido(String nombre) throws Exception {
        URL defaultSound=getClass().getResource("/Sonidos/" + nombre);
        AudioInputStream soundIn = AudioSystem.getAudioInputStream(defaultSound);
        AudioFormat format = soundIn.getFormat();
        DataLine.Info info = new DataLine.Info(Clip.class, format);

        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(soundIn);
        //clip.start();
        clip.loop(1);
        {
            //    Thread.yield();
        }
        //clip.stop();

    }
}
