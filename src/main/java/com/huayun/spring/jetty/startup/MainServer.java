package com.huayun.spring.jetty.startup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Properties;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class MainServer {

	private static Logger logger = Logger.getLogger(MainServer.class);

	public static void main(String[] args) {
		try {
			// 读取服务器配置
			Resource resource = new FileSystemResource("conf/jetty-server.properties");
			Properties properties = null;
			try {
				properties = PropertiesLoaderUtils.loadProperties(resource);
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 配置
			String host = properties.getProperty("jetty.server.host");
			String port = properties.getProperty("jetty.server.port");
			String context = properties.getProperty("jetty.server.context");
			String webappPath = properties.getProperty("jetty.server.webapp.path");
			// 服务创建启动
			Server server = createServer(host, port, context, webappPath);
			server.start();
			server.join();
		} catch (Exception e) {
			logger.error("start MainServer error", e);
			System.exit(1);
		}

	}

	/**
	 * 创建Server
	 * 
	 * @param host
	 *            主机的地址
	 * @param port
	 *            端口号
	 * @param context
	 *            项目App名称
	 * @param webappPath
	 *            webApp的路劲位置(即就是war包路劲)
	 * @return
	 */
	public static Server createServer(String host, String port, String contextPath,	String webappPath) {
		Server server = new Server();
		server.setStopAtShutdown(true);

		ProtectionDomain protectionDomain = MainServer.class.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();

		WebAppContext context = new WebAppContext(webappPath, contextPath);
		context.setServer(server);

		// 设置work dir,war包将解压到该目录，jsp编译后的文件也将放入其中。
		String currentDir = new File(location.getPath()).getParent();
		File workDir = new File(currentDir, "work");
		context.setTempDirectory(workDir);

		server.setHandler(context);
		// no ssl
		SelectChannelConnector selectChannelConnector = new SelectChannelConnector();
		selectChannelConnector.setPort(NumberUtils.toInt(port));
		// ssl
		/*SslSelectChannelConnector sslSelectChannelConnector = new SslSelectChannelConnector();
		sslSelectChannelConnector.setPort(NumberUtils.toInt(port));
		SslContextFactory cf = sslSelectChannelConnector.getSslContextFactory();
		cf.setKeyStorePath("/usr/cetc32/hippo/hippo_task_broker/keystore");
		String obfPwd = Password.obfuscate("password");
		cf.setKeyStorePassword(obfPwd);
		cf.setKeyManagerPassword(obfPwd);*/
		server.setConnectors(new Connector[] { selectChannelConnector });
		return server;
	}

}
