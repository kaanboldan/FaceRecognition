package YuzTanitma;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
//opencv 4.30
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

import javafx.stage.Stage;

public class AnasayfaController {
	//tanimlamalar
	Date tarih=new Date();
	SimpleDateFormat ft=new SimpleDateFormat("yyyy.MM.dd'_at_'hh.mm.ss'_'a'_'zzz");
	int bulunanyuz=0;
	//goruntu elemanlari
    @FXML
    private Button getir_btn;

    @FXML
    private Label sonuc_txt;

    @FXML
    private Button gecis_btn;

    @FXML
    void gecis_btn_click(ActionEvent event) throws IOException {
    	//gecis butonuna tiklanildiginda kayit ekranina gecis yapilir.
    	Parent home_pageParent=FXMLLoader.load(getClass().getResource("YuzTanitma.fxml"));
    	Scene home_pageScene =new Scene(home_pageParent);
    	Stage app_Stage=(Stage) ((Node) event.getSource()).getScene().getWindow();
    	//anasayfa gizlenir
    	app_Stage.hide();
    	app_Stage.setScene(home_pageScene);
    	//kayit ekrani gosterilir.
    	app_Stage.show();
    }

    @FXML
    void getir_btn_click(ActionEvent event) {
    	
		//open cvkutuphaneleri yuklenir.
    	System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    	VideoCapture vc =new VideoCapture();
		//hangi kamera istendigi buradan ayarlanir.
    	vc.open(0);
    	
		
		if(vc.isOpened())
    		{
				System.out.println("fotograf cekiliyor");
				String saat=ft.format(tarih);	
				Mat mat=new Mat();
				vc.read(mat);
				Imgcodecs.imwrite("img/cekilen_resim/login_"+saat+".png", mat);
				vc.release();
    		}
		else
		{System.out.println("!!!Kameraya baglanilamadi!!!");}
		
		bulunanyuz=okanboldan();
		
		//eger yuz bulunamazsa baska yuzler denenir
		
		if(bulunanyuz==0)
		{
		 bulunanyuz=kaanboldan();
		}
		
		
		//eger yuz bulunmaz ise burasi calisir.
		if(bulunanyuz==0)
			{
				System.out.println("yuz bulunamadi");
				//kisi bulunamadigi icin kullanici bilgilendirilir.
				sonuc_txt.setText("Kisi Bulunmadi, Tekrar deneyin");
			}
				
		System.out.println("yuz tanimlama tamamlandi");
			}
    

	private int okanboldan() 
		{
				//okan adli kisinin cascadesi ile benzerlige bakilir
				String saat=ft.format(tarih);		
				String imgFile = "img/cekilen_resim/login_"+saat+".png";
				Mat src = Imgcodecs.imread(imgFile);
				String xmlFile = "Araclar/xml/okan_cascade.xml";
			 	CascadeClassifier cc = new CascadeClassifier(xmlFile);
				MatOfRect faceDetection = new MatOfRect();
				cc.detectMultiScale(src, faceDetection);
				System.out.println(String.format("okan: %d", faceDetection.toArray().length));
				for(Rect rect: faceDetection.toArray())
				{
					//okanin yuzu kare icine alinir ve isaretlenir.
					Imgproc.putText(src, "Okan", new Point(rect.x, rect.y-5), 1,2 , new Scalar(0, 0, 255));
					Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height) , new Scalar(0, 0, 255), 3);
					//resimde bulunan yuzler kesilir ve kesilmis klasorune atilir.
					Mat kes =new Mat(src, rect);
					Imgcodecs.imwrite("img/kesilmis/"+saat+".png", kes);
				}
				
				if(faceDetection.toArray().length!=0)
				sonuc_txt.setText("Okan BOLDAN");
				return faceDetection.toArray().length;
		
		}
	
	private int kaanboldan() 
		{
			// kaanboldan adli kisinin cascadesi ile benzerlige bakilir.
			String saat=ft.format(tarih);	
			String xmlFile = "Araclar/xml/kaan_cascade.xml";
			CascadeClassifier cc = new CascadeClassifier(xmlFile);
			String imgFile = "img/cekilen_resim/login_"+saat+".png";
			Mat src = Imgcodecs.imread(imgFile);
			MatOfRect faceDetection = new MatOfRect();
			cc.detectMultiScale(src, faceDetection);
			System.out.println(String.format("kaan: %d", faceDetection.toArray().length));
		
			//eger kamera karsisinda kaanboldan varsa labela kaan boldan yazilir
			if(faceDetection.toArray().length!=0)
			sonuc_txt.setText("Kaan BOLDAN");
			
			for(Rect rect: faceDetection.toArray())
				{
					//kaanin yuzu kare icine alinir ve isaretlenir.
					Imgproc.putText(src, "Kaan", new Point(rect.x, rect.y-5), 1,2 , new Scalar(0, 0, 255));
					Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height) , new Scalar(0, 0, 255), 3);
					//resimde bulunan yuzler kesilir ve kesilmis klasorune atilir.
					Mat kes =new Mat(src, rect);
					Imgcodecs.imwrite("img/kesilmis/"+saat+".png", kes);
				}
			 
				Imgcodecs.imwrite("img/islenen_resim/"+saat+".png", src);
				
				return faceDetection.toArray().length;
		}
	
	
	
	

    }
    
