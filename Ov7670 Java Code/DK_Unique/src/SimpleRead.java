import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class SimpleRead {
 private static final  char[]COMMAND = {'*', 'R', 'D', 'Y', '*'};
 private static final int WIDTH = 320; // 640;
    private static final int HEIGHT = 240; // 480;
   
    private static CommPortIdentifier portId;
    InputStream inputStream;
    SerialPort serialPort;

    public static void main(String[] args) {
      Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
         portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
             System.out.println("Port name : " + portId.getName());
                if (portId.getName().equals("COM5")) { // port kontrolü
                 SimpleRead reader = new SimpleRead();
                }
            }
        }
    }

    public SimpleRead() throws UnsupportedCommOperationException {
        int[][] rgb = new int[HEIGHT][WIDTH];
        int[][] rgb2 = new int[WIDTH][HEIGHT];
     
	try { // HATA KONTROL BLOĞU
            serialPort = (SerialPort) portId.open("SimpleReadApp", 1000);
            inputStream = serialPort.getInputStream();
            serialPort.setSerialPortParams(1000000,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            int counter = 0;
            while (true) {
                System.out.println("Looking for image");
                while (!isImageStart(inputStream, 0)) {
                }
		System.out.println("Found image : " + counter); // Çekilen Resim Sayısı
                for (int y = 0; y < HEIGHT; y++) {
                    for (int x = 0; x < WIDTH; x++) {
                        int temp = read(inputStream);
                        rgb[y][x] = ((temp & 0xFF) << 16) | ((temp & 0xFF) << 8) | 							(temp & 0xFF);
                    }
                }

                for (int y = 0; y < HEIGHT; y++) {
                    for (int x = 0; x < WIDTH; x++) {
                        rgb2[x][y] = rgb[y][x];

                    }
                }

                  // dosya adı için zaman bilgisi elde etme
                DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();
                
                // bmp formatında resim kaydetme
                BMP bmp = new BMP();
                bmp.saveBMP("C:/Users/DK/Downloads/dk/" + sdf.format(date) + ".bmp", rgb2);
                System.out.println("Saved image : " + counter);
                // mail için dosya kontrolü
              String filename = "C:/Users/DK/Downloads/dk/" + sdf.format(date) + ".bmp";
              File file = new File(filename);
              if (!file.exists()) {
                  System.out.println("File Not Created");
              }
              // toplu mail için kullanıcıları tanımlama ve mail ayarlarını yapmak
              ArrayList<String> mailaddress = new ArrayList<String>();
              mailaddress.add("javaapachetestmail@gmail.com");
              mailaddress.add("azatates4977@gmail.com"); 
      
       for (String mail : mailaddress) {
            	  Date dt = new Date();
                  HtmlEmail email = new HtmlEmail(); // email nesnesi yaratılıyor
                  email.setHostName("smtp.gmail.com"); // host ayarı tanımlama
                  email.setSmtpPort(465); // port bilgisi tanýmlama
                  email.setAuthenticator(new DefaultAuthenticator
                       ("testmailjavaapache@gmail.com", "Apachemail1234"));
                // güvenlik ayarları yapılmış yönlendirme servisi
                  email.setSSLOnConnect(true); // ssl desteğini aktif etme
                  email.setFrom("testmailjavaapache@gmail.com"); 

                  email.setSubject("Test Mail"); // konu
                  email.setMsg("Test Mail Sended"); // mesaj
                  email.attach(file); // gönderilecek dosya

                  email.addTo(mail); // mail adresi bilgisi ekleme
                  email.send(); // gönder
                  System.out.println("File Sended Succesfully  "+dt); 
                }
            }
        }
        catch (IOException | EmailException | PortInUseException ex) {
            System.out.println("Hata Algılandı : " + ex.getMessage()); 
        }
    }
    
    private void copyFileUsingApacheCommonsIO(File path, File path1) throws IOException {
 
     Files.copy(path.toPath(), path1.toPath());
 }

 private int read(InputStream inputStream) throws IOException {
     int temp = (char) inputStream.read();
  if (temp == -1) {
   throw new  IllegalStateException("Exit");
  }
  return temp;
    }
     
    private boolean isImageStart(InputStream inputStream, int index) throws IOException {
     if (index < COMMAND.length) {
      if (COMMAND[index] == read(inputStream)) {
       return isImageStart(inputStream, ++index);
      } else {
       return false;
      }
     }
     return true;
    }
}
