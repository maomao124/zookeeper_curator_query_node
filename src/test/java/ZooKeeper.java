import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Project name(项目名称)：zookeeper_curator查询节点
 * Package(包名): PACKAGE_NAME
 * Class(类名): ZooKeeper
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/4/20
 * Time(创建时间)： 21:17
 * Version(版本): 1.0
 * Description(描述)： 无
 */

public class ZooKeeper
{
    private CuratorFramework client;

    @BeforeEach
    void setUp()
    {
        //重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        //zookeeper创建链接，第一种
                        /*
                        CuratorFramework client =
                                CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                                        60 * 1000,
                                        15 * 1000,
                                        retryPolicy);
                        client.start();
                        */

        //zookeeper创建链接，第二种
        client = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(60 * 1000)
                .connectionTimeoutMs(15 * 1000)
                .retryPolicy(retryPolicy)
                .namespace("test")
                .build();
        client.start();
    }

    @AfterEach
    void tearDown()
    {
        if (client != null)
        {
            client.close();
        }
    }

    @Test
    void test1() throws Exception
    {
        byte[] app1s = client.getData().forPath("/app1");
        System.out.println(new String(app1s, StandardCharsets.UTF_8));
        client.getData().forPath("/app2");
        System.out.println(new String(app1s, StandardCharsets.UTF_8));
        client.getData().forPath("/app3");
        System.out.println(new String(app1s, StandardCharsets.UTF_8));
        client.getData().forPath("/app4");
        System.out.println(new String(app1s, StandardCharsets.UTF_8));
        client.getData().forPath("/app5/a1");
        System.out.println(new String(app1s, StandardCharsets.UTF_8));
    }

    @Test
    void test2() throws Exception
    {
        List<String> list = client.getChildren().forPath("/");
        System.out.println(list);
        List<String> list1 = client.getChildren().forPath("/app5");
        System.out.println(list1);
    }

    @Test
    void test3() throws Exception
    {
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath("/app4");
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
        System.out.println(stat);
        /*
        Stat参数：
        private long czxid;
        private long mzxid;
        private long ctime;
        private long mtime;
        private int version;
        private int cversion;
        private int aversion;
        private long ephemeralOwner;
        private int dataLength;
        private int numChildren;
        private long pzxid;
        */
    }

}
