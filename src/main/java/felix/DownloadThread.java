package felix;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Author: EN.Felix
 * Email: Javagoshell@gmail.com
 */
public class DownloadThread extends Thread {
    private int startPos;
    private int partSize;
    public volatile int length;
    private String path;
    private RandomAccessFile randomAccessFile;

    public DownloadThread(String path, int startPos, int partSize, RandomAccessFile randomAccessFile) {
        this.path = path;
        this.startPos = startPos;
        this.partSize = partSize;
        this.randomAccessFile = randomAccessFile;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.skip(startPos);
            int b;
            while (length < partSize && (b = inputStream.read()) != -1) {
                length++;
                randomAccessFile.write(b);
            }
            randomAccessFile.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
