import com.buis.common.manager.RedisLimiterManager;
import com.buis.service.impl.AiServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@SpringBootTest
public class rateLimiterTest {
    @Resource
    private RedisLimiterManager redisLimiterManager;
    @Resource
    private AiServiceImpl aiService;

    @Test
    void doRateLimit() throws InterruptedException {
        String userId = "1";
        for (int i = 0; i < 2; i++) {

            redisLimiterManager.doRateLimit(userId);
            System.out.println("成功");
        }

    }


    @Test
    public Flux<String> chatWithAgent(@RequestParam("message") String message,
                                      @RequestParam(required = false) String threadId, HttpServletResponse response) {
        //UTF-8
        response.setCharacterEncoding("UTF-8");
        //流式返回
        return aiService.agentCallWithMessage("你好", threadId);

    }

}
