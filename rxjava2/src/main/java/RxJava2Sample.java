import io.reactivex.*;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author tao.yang
 * @date 2020-06-03
 */
public class RxJava2Sample {

    private static final Logger logger = LoggerFactory.getLogger(RxJava2Sample.class);

    public static void main(String[] args) {
        Flowable.just("Hello world").subscribe(System.out::println);

        FlowableSubscriber<String> subscriber = new FlowableSubscriber<String>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                logger.info("onNext => {} ", s);
            }

            @Override
            public void onError(Throwable throwable) {
                logger.error("onError => {}", throwable);
            }

            @Override
            public void onComplete() {
                logger.info("onComplete");
            }
        };

        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> flowableEmitter) throws Exception {
                flowableEmitter.onNext("next-1");
                flowableEmitter.onNext("next-2");
                flowableEmitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        flowable.subscribe(subscriber);
    }
}
