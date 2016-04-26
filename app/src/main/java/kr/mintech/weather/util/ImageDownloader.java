package kr.mintech.weather.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageDownloader {
	
	public static Bitmap getImageFromUrl(String url) throws IOException
	{
		Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
		return bitmap;
	}
	

}
