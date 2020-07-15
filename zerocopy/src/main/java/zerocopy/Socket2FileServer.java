package zerocopy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * zero copy server
 * @author tao.yang
 * @date 2020-07-15
 */
public class Socket2FileServer {

    private static Logger logger = LoggerFactory.getLogger(Socket2FileServer.class);

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        logger.info("监听 {} ", "8080");

        while (selector.select() > 0) {
            logger.info(" 收到请求");
            SocketChannel socketChannel = serverSocketChannel.accept();
            File targetFile = new File("/Users/yangtao/IU-173.4674.33_yangtao_03.01.2020_14.03.14.zip.zerocopy-" + System.currentTimeMillis());
            try (FileOutputStream out = new FileOutputStream(targetFile)) {
                // 使用out.getChannel().transferFrom 将 socket channel 的内容写入到磁盘当中
                out.getChannel().transferFrom(socketChannel, 0, Long.MAX_VALUE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("接收文件完成 {} ", targetFile.getName());
            socketChannel.close();
        }
    }
}
