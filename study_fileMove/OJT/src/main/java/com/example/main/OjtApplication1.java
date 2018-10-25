package com.example.main;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import com.example.base.BaseRun;
import com.example.init.InitCheckFile;
import com.example.service.MoveService;

/*
 * point1: springのcomponentScanの設定をxmlで行っている。 point2:外部ファイルの活用及び設定。point3: classpathのデフォルト位置
 */

@EnableAutoConfiguration
@ImportResource("classpath:springmvc-config.xml")
@PropertySources({ @PropertySource("classpath:directory.properties"), @PropertySource("classpath:csv.properties") })
public class OjtApplication1 extends InitCheckFile {
	private static final Logger log = LoggerFactory.getLogger(OjtApplication1.class);
	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(OjtApplication1.class);
		application.setWebEnvironment(false);
		ApplicationContext context = application.run(args);
		SpringApplication.exit(context);

	}

	@Override
	public void runDetails() {

		/*
		 * point1: 前処理(init)と実処理(exec)のように処理を分割(オブジェクト化)。 point2:if文の判定に処理を記載する。point3:
		 * 変則的なif文記載方法
		 * 
		 */
		baseDir = env.getProperty("baseDir");
		opponentDir = env.getProperty("moveDir");
		try {
			File[] files = init(baseDir, opponentDir);
			if (files != null)
				exec(files);
			else
				log.error("失敗");
		} catch (Exception e) {
			log.error("失敗:" + e);
		}
	}

	@Autowired
	MoveService execService;

	public Boolean exec(File[] files) {
		/*
		 * point1:命名規則にisSuccessのような実行結果を表す変数名を作成する 。
		 */
		Boolean isSuccess = false;
		isSuccess = execService.execDetails(files);
		return isSuccess;
	}
}
