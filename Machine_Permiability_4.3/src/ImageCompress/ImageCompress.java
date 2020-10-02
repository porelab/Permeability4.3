package ImageCompress;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class ImageCompress {



	
	public static File getCompressCover(File input)
	{
		try
		{ 
			
			//  File input = new File("pics/c1.jpg");

			 
			  double size=(double) input.length()/1024;
			  
			  if(size>500)
			  {
			  
			  System.out.println(size+"  kb");
			
			  System.out.println(input.getName());
			  
		  
		      BufferedImage image = ImageIO.read(input);

		      
		      File mainfolder=new File("photos");
		      if(!mainfolder.exists())
		      {
		    	  mainfolder.mkdir();
		      }
		      File folder=new File("photos/coverpic");
		      if(!folder.exists())
		      {
		    	  folder.mkdir();
		      }
		      
		      
		      File compressedImageFile = new File("photos/coverpic/"+input.getName());
		      OutputStream os =new FileOutputStream(compressedImageFile);

		      Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
		      ImageWriter writer = (ImageWriter) writers.next();

		      ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		      writer.setOutput(ios);

		      ImageWriteParam param = writer.getDefaultWriteParam();
		      
		      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		      param.setCompressionQuality(0.2f);
		      writer.write(null, new IIOImage(image, null, null), param);
		      
		      os.close();
		      ios.close();
		      writer.dispose();
			
		      
		      return compressedImageFile;
		      
			  }
			  else
			  {
				  return input;
			  }
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return input;
			
		}
	}
	
	public static File getCompressImage(File input)
	{
		try
		{ 
			
			//  File input = new File("pics/c1.jpg");

			 
			  double size=(double) input.length()/1024;
			  
			  if(size>500)
			  {
			  
			  System.out.println(size+"  kb");
			
			  System.out.println(input.getName());
			  
		  
		      BufferedImage image = ImageIO.read(input);

		      
		      File mainfolder=new File("photos");
		      if(!mainfolder.exists())
		      {
		    	  mainfolder.mkdir();
		      }
		      File folder=new File("photos/sampleimages");
		      if(!folder.exists())
		      {
		    	  folder.mkdir();
		      }
		      
		      
		      File compressedImageFile = new File("photos/sampleimages/"+input.getName());
		      OutputStream os =new FileOutputStream(compressedImageFile);

		      Iterator<ImageWriter>writers =  ImageIO.getImageWritersByFormatName("jpg");
		      ImageWriter writer = (ImageWriter) writers.next();

		      ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		      writer.setOutput(ios);

		      ImageWriteParam param = writer.getDefaultWriteParam();
		      
		      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		      param.setCompressionQuality(0.1f);
		      writer.write(null, new IIOImage(image, null, null), param);
		      
		      os.close();
		      ios.close();
		      writer.dispose();
			
		      
		      return compressedImageFile;
		      
			  }
			  else
			  {
				  return input;
			  }
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return input;
			
		}
	}
	
}
