package async.servlet;


import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author tao.yang
 * @date 2020-07-14
 */
@WebServlet(name = "MyAsyncServlet", urlPatterns = {"/async-process"}, asyncSupported = true)
public class MyAsyncServlet extends HttpServlet {

    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(1, 2, 1000,
            TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(10),
            Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        OutputStream out = resp.getOutputStream();
        AsyncContext asyncContext = req.startAsync(req, resp);

        // 使用线程处理请求
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String json = "json string";
                try {
                    out.write(json.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                asyncContext.complete();
            }
        };

        // 使用线程池来执行请求
        poolExecutor.submit(runnable);
        super.doGet(req, resp);
    }
}
