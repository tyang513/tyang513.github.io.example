package async.servlet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author tao.yang
 * @date 2020-07-06
 */
@RestController
public class AsyncController {

    private static final Logger logger = LoggerFactory.getLogger(AsyncController.class);

    // private BoundRequestBuilder builder;

    /**
     * 异步阻塞式
     * @return
     */
    @GetMapping(value = "/v1")
    public Callable indexV1(){
        long startTime = System.currentTimeMillis();
        logger.info("indexV1");
        Callable<String> returnCallable = () -> {
            logger.info("callable trhead id {}", Thread.currentThread().getName());
            Thread.sleep(10000);
            return "indexV1";
        };

        logger.info("Latency {}", (System.currentTimeMillis() - startTime) / 1000 );
        return returnCallable;
    }

    /**
     * 异步非阻塞式
     * @param request
     * @return
     */
    @GetMapping(value = "/v2")
    public CompletableFuture<String> indexV2(ServletRequest request){
        long startTime = System.currentTimeMillis();
        logger.info("indexV1");
        ListenableFuture<String> listenableFuture = null;
//        ListenableFuture<String> listenableFuture = request.execute(new AsyncCompletionHandler<String>() {
//            public String onCompleted(Response response) throws Exception {
//                logger.debug("Async Non Blocking Request processing completed");
//                return "Async Non blocking...";
//            }
//        });

        logger.info("Latency {}", (System.currentTimeMillis() - startTime) / 1000 );
        return null; //listenableFuture.addCallback();
    }

}
