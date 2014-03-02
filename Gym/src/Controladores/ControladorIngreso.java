/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import BD.ConexionBD;
import Interfaces.IngresoGui;
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
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationEvent;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationListener;
import com.digitalpersona.onetouch.ui.swing.DPFPVerificationVetoException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.awt.Color;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;

/**
 *
 * @author Nico
 */
public class ControladorIngreso implements ActionListener{
//Varible que permite iniciar el dispositivo de lector de huella conectado
// con sus distintos metodos.

    private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();

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
    
    public ControladorIngreso(IngresoGui ingresoGui) {
        this.ingresoGui=ingresoGui;
        ingresoGui.limpiar();
        ingresoGui.setActionListener(this);
        Iniciar();
        start();
                  
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
                Image image=CrearImagenHuella(sample);
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
        this.template= template;
    }

    
     public void identificarHuella() throws IOException{
     try {
      Connection c = con.conectar();
            String query = "SELECT * FROM huellas ";
            PreparedStatement stmt = c.prepareStatement(query);
            //stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery(query);
            //Ejecuta la sentencia
            if (rs.next()) {
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
       if (result.isVerified()){
       //crea la imagen de los datos guardado de las huellas guardadas en la base de datos
       EnviarTexto("Verificación correcta,la huella capturada es de "+idCliente);
       //Aca cargo toda la gilada del usuario
       return;
                               }
       }
       //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje

        EnviarTexto("No existe ningún registro que coincida con la huella");
       setTemplate(null);
       ingresoGui.noIdentifado();
        } catch (SQLException e) {
       //Si ocurre un error lo indica en la consola
       System.err.println("Error al identificar huella dactilar."+e.getMessage());
       }finally{
       con.desconectar();
       }
   }
     
       public  Image CrearImagenHuella(DPFPSample sample) {
	return DPFPGlobal.getSampleConversionFactory().createImage(sample);
}

  public void DibujarHuella(Image image) {
        ingresoGui.getImHuella().setIcon(new ImageIcon(
        image.getScaledInstance(ingresoGui.getImHuella().getWidth(), ingresoGui.getImHuella().getHeight(), Image.SCALE_DEFAULT)));
        ingresoGui.repaint();
 }
     
      public static void main(String... args) {
          IngresoGui ing= new IngresoGui();
          ControladorIngreso cr= new ControladorIngreso(ing);
          ing.setVisible(true);
          
      }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== ingresoGui.getLimpiarVentana()){
            System.out.println("limpiar ventana");
            ingresoGui.limpiar();
        }
        if(e.getSource()==ingresoGui.getBusquedaManual()){
            System.out.println("busqueda manual");
        }
    }
      
}
