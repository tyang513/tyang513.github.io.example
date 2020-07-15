package zerocopy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * zero copy client
 * @author tao.yang
 * @date 2020-07-15
 */
public class File2SocketClient {

    private static Logger logger = LoggerFactory.getLogger(File2SocketClient.class);

    public static void main(String[] args) throws IOException {

        SocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 8080);

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(serverAddress);
        socketChannel.configureBlocking(true);

        File transferFile = new File("/Users/yangtao/IU-173.4674.33_yangtao_03.01.2020_14.03.14.zip");

        FileChannel fileChannel = new FileInputStream(transferFile).getChannel();

        logger.info("fileName {} fileSize {} fileLength {}", transferFile.getName(), fileChannel.size(), transferFile.length());

        // 使用 fileChannel.transferTo 将磁盘文件写到 socket channel
        fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        logger.info("fileName {} fileSize {}", transferFile.getName(), fileChannel.size());
         fileChannel.close();
    }

}
