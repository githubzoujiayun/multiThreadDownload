package felix;

import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Author: EN.Felix
 * Email: Javagoshell@gmail.com
 */
public class DownloadUtil {
    private int threadNum;
    private int contentLength;
    private String targetFileName;
    private String path;
    private DownloadThread[] downloadThreads;

    public DownloadUtil(String path, int threadNum, String targetFileName) {
        this.path = path;
        this.threadNum = threadNum;
        this.targetFileName = targetFileName;
        downloadThreads = new DownloadThread[threadNum];
    }

    public void download() throws Exception {
        URL url = new URL(path);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        contentLength = httpURLConnection.getContentLength();
        int partSize = contentLength / threadNum + 1;
        RandomAccessFile randomAccessFile = new RandomAccessFile(targetFileName, "rw");
        randomAccessFile.setLength(contentLength);
        randomAccessFile.close();
        httpURLConnection.disconnect();
        for (int i = 0; i < threadNum; i++) {
            int startPos = i * partSize;
            RandomAccessFile randomAccessFile1 = new RandomAccessFile(targetFileName, "rw");
            randomAccessFile1.seek(startPos);
            downloadThreads[i] = new DownloadThread(path, startPos, partSize, randomAccessFile1);
            downloadThreads[i].start();
        }
    }

    public double getComplete() {
        double sum = 0;
        for (int i = 0; i < threadNum; i++) {
            sum += downloadThreads[i].length;
        }
        return sum * 1.0 / contentLength;
    }
}
