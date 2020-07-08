package async.servlet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/**
 * @author tao.yang
 * @date 2020-07-06
 */
@RestController
public class AsyncController {

    private static final Logger logger = LoggerFactory.getLogger(AsyncController.class);

    @GetMapping(value = "/index")
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

}
