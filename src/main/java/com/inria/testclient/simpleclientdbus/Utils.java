package com.inria.testclient.simpleclientdbus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author cgourdin
 */
public class Utils {
    /**
	 * Close quietly an inputstream without exception thrown.
	 * 
	 * @param in
	 */
	public static void closeQuietly(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				/* ignore */ }
		}
	}

	/**
	 * Close quietly an outputstream without exception thrown.
	 * 
	 * @param os
	 */
	public static void closeQuietly(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
				/* ignore */ }
		}
	}
    
    /**
	 * Simple copy a stream with a buffer of 1024 bytes into an outputstream.
	 * 
	 * @param in
	 * @param os
	 * @return a String representation of copied bytes, null if outputstream is not a ByteArrayOutputStream.
     * @throws IOException
     * 
	 */
	public static String copyStream(InputStream in, OutputStream os) throws IOException {
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			os.write(buf, 0, len);
		}
        os.flush();
        if (os instanceof ByteArrayOutputStream) {
            return new String(((ByteArrayOutputStream) os).toByteArray(), "UTF-8");
        }
        return null;
	}
}
