package cn.luckydeer.auto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import cn.luckydeer.auto.constants.AutoToolConstants;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

/**
 * 自动生成代码工具
 * 
 * @author yuanxx
 * @version $Id: App.java, v 0.1 2018年6月26日 下午7:56:48 yuanxx Exp $
 */
public class AutoCodeTool {

    /**
     * 
     * 注解：根据配置文件自动生成Dao测试代码
     * @author yuanxx @date 2018年6月27日
     * @throws Exception 
     */
    @Test
    public void getDaoTest() throws Exception {
        /** 1.读取配置文件  */
        Properties properties = new Properties();
        // 使用ClassLoader加载properties配置文件生成对应的输入流
        InputStream in = AutoCodeTool.class.getClassLoader().getResourceAsStream(
            "AutoCode.properties");
        // 使用properties对象加载输入流
        properties.load(in);

        /** 2. FreeMarker 配置  */
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        /** 3. 设置模板存放路径 */
        cfg.setDirectoryForTemplateLoading(new File(properties
            .getProperty(AutoToolConstants.TEMPLATE_PATH)));
        /** 4. 默认编码类型   默认为UTF-8 */
        cfg.setDefaultEncoding(AutoToolConstants.DEFAULT_ENCODE);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        /** 5.封装参数  */
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("classPath", properties.get(AutoToolConstants.PACKAGE_NAME));
        dataMap.put("className", properties.get(AutoToolConstants.CLASS_NAME));

        /** 7.获取模板  */
        Template template = cfg.getTemplate(AutoToolConstants.DAO_TEST_TEM_NAME);

        /** 8.生成数据  */
        File docFile = new File(properties.getProperty(AutoToolConstants.STORE_PATH) + "\\"
                                + properties.getProperty(AutoToolConstants.CLASS_NAME) + ".java");
        /** 9.写出流  */
        Writer out = null;
        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(docFile)));
        template.process(dataMap, out);
        System.out.println("java 文件创建成功 !");
    }

}
