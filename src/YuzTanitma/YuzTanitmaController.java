package YuzTanitma;

import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class YuzTanitmaController {

    @FXML
    private ImageView imv_foto;

    @FXML
    private Button cek_btn;

    @FXML
    private TextField ad_txt;
    
    @FXML
    private Label lbl_kalan;
    
    @FXML
    private Button anasayfa_btn;

    @FXML
    void anasayfa_btn_click(ActionEvent event) throws IOException 
	    {	
    		//tiklandiginda ana sayfaya gitmektedir.
	    	Parent home_pageParent=FXMLLoader.load(getClass().getResource("Anasayfa.fxml"));
	    	Scene home_pageScene =new Scene(home_pageParent);
	    	Stage app_Stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
	    	app_Stage.hide();
	    	app_Stage.setScene(home_pageScene);
	    	app_Stage.show();
	    }

    @FXML
    void cek_btn_click(ActionEvent event) {
    	
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	if(ad_txt.getText().equals(""))
    	{System.out.println("bos gecilemez");}
    		
    	else
    	{
    			VideoCapture vc =new VideoCapture();
	    		vc.open(0);
	    		if(vc.isOpened())
		    		{
	    				Mat mat=new Mat();
		    			//10 adet foto cekiliyor.
		    			for (int a = 1; a <=10; a++) 
			        		{	
				    			System.out.println(a+".fotograf cekiliyor");
					    		vc.read(mat);
								Imgcodecs.imwrite("img/cekilen_resim/"+ad_txt.getText()+"_"+a+".png", mat);
								lbl_kalan.setText(Integer.toString(a));
								
			        		}
		    			vc.release();
		    			for (int b = 1; b <=10; b++) 
							{
				    			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				    			System.out.println(b+".foto isleniyor");
								String imgFile = "img/cekilen_resim/"+ad_txt.getText()+"_"+b+".png";
								Mat src = Imgcodecs.imread(imgFile);
								
								String xmlFile = "Araclar/xml/lbpcascade_frontalface.xml";
								CascadeClassifier cc = new CascadeClassifier(xmlFile);
								MatOfRect faceDetection = new MatOfRect();
								cc.detectMultiScale(src, faceDetection);
								System.out.println(String.format("bulunan yuzler %d", faceDetection.toArray().length));
								
								
								for(Rect rect: faceDetection.toArray())
									{
										Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height) , new Scalar(0, 0, 255), 3);
										
										Mat kes =new Mat(src, rect);
										Imgcodecs.imwrite("img/kesilmis/"+ad_txt.getText()+"_"+b+".png", kes);
									}
										
								if(faceDetection.toArray().length==0)
									{
										System.out.println("yuz bulunamadi");
										
									}
									System.out.println("---------------");
							
										Imgcodecs.imwrite("img/islenen_resim/"+ad_txt.getText()+"_"+b+".png", src);
							}
									System.out.println("yuz tanimlama tamamlandi");
								
					}
	    		else 
	    		{
	    			System.out.println("Kamera acilamadigi icin fotograf cekilemedi.");
	    		}
		    		}
	    		
    		}
    		
    		
    		
    	}
    	
   
    
    

