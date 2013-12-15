package gigs.engine.event.user;

import gigs.db.cloudsql.ImageWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Date;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ServingUrlOptions;

@SuppressWarnings("unused")
public final class EventImage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 270429739252351481L;
	public String IID = "";
	public BlobKey key; // = new BlobKey("");
	public EventImageType type; 
	public int thumbsize;
	public int ownerID;
	
	private ImageWrapper iwrap = new ImageWrapper();
	
	private static final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	private static final ImagesService imagesService = ImagesServiceFactory.getImagesService();
	private static final String URL_PREFIX = "http://stageous.com";
	
	 public EventImage(BlobKey key, EventImageType t, int OID) // UPLOAD CREATE
	 {
		  type = t;
		  IID = this.createIID();
		  ownerID = OID;
		  this.key = key;
		  IID = IID + key.getKeyString();
		  iwrap.addImage(this);
	 }
	 
	 public EventImage(BlobKey key, EventImageType t, int OID, String IID) 
	 {
		  type = t;
		  this.key = key;
		  IID = this.createIID();
		  ownerID = OID;
		  this.IID = IID;
	 }
	 
	 public EventImage(URL u, EventImageType t, int OID) // HTTP CREATE
	 {
		  type = t;
		  IID = this.createIID();
		  ownerID = OID;
		  this.httpBlob(u);
		  IID = IID + key.getKeyString();
		  iwrap.addImage(this);
	}
	
	public EventImage(EventImageType t, int OID, String IID) throws IOException
	{
		type = t;
		this.IID = IID;
		ownerID = OID;
	}
	
	public void httpBlob(URL u)
	{
		try{
			HttpURLConnection connection = (HttpURLConnection) u.openConnection();
			connection.setRequestMethod("GET");
			String mimetype = connection.getContentType();
			System.out.println("MIME type is: " + mimetype);
			String filetype = mimetype.substring( mimetype.indexOf('/') );
			String filename = this.IID + new Timestamp(new Date().getTime()).getTime() + "." + filetype;
			
			InputStream img = connection.getInputStream();
			int len = connection.getContentLength();
			byte[] imagedata = new byte [len]; 
			
	//		img.read(imagedata, 0, len);
			System.out.println("Number of bytes read: " + img.read(imagedata, 0, len));
			Image image = ImagesServiceFactory.makeImage(imagedata);
			System.out.println("Image height : " + image.getHeight() +  ", width: " + image.getWidth());
			System.out.println("Length: " + imagedata.length + " or " + image.getImageData().length);
			
			// TODO - change thumb sizes
	//		int width = 500;
	//		int height = 900;
	//		Transform resize = ImagesServiceFactory.makeResize(width, height, false);
	//
	//        image = imagesService.applyTransform(resize, image);
	        
	        // Get a file service
	        FileService fileService = FileServiceFactory.getFileService();
	
	        // Create a new Blob file with mime-type "text/plain"
	        AppEngineFile file = fileService.createNewBlobFile(mimetype, filename);
	        //this.key = fileService.getBlobKey(file);
	        System.out.println("1 Blob key is: " + key);
	        // Open a channel to write to it
	        boolean lock = true;
	        FileWriteChannel writeChannel = fileService.openWriteChannel(file, lock);
	        
	        writeChannel.write(ByteBuffer.wrap(image.getImageData()));
	        writeChannel.closeFinally();
	        
	        lock = false;
	
	        this.key = fileService.getBlobKey(file);
	        System.out.println("2 Blob key is: " + key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadBlob()
	{
//		return null;
	}
	
	public String createIID()
	{
		String temp = "stageous_";
		switch (type)
		{
			case EVENTUSERIMAGE:
			{
				this.thumbsize = 100;
				temp += "evn";
			}
			case PROFILEIMAGE:
			{
				this.thumbsize = 100;
				temp += "prf";	
			}
			case EVENTFMIMAGE:
			{
				this.thumbsize = 100;
				temp += "evl";
			}
		}
		return temp;
	}
	
	public URL toURL()
	{
		ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(this.key);
		try {
			return new URL(imagesService.getServingUrl(options));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public URL toThumbURL()
	{
		ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(this.key);
		options.crop(true);
		try {
			return new URL(imagesService.getServingUrl(options.imageSize(this.thumbsize)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public URL toThumbURL(int ts)
	{
		ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(this.key);
		options.crop(true);
		try {
			return new URL(imagesService.getServingUrl(options.imageSize(ts)));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

