package com.example.main;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import com.example.init.InitCheckFile;
import com.example.service.InputService;

/*
 * point1: springのcomponentScanの設定をxmlで行っている。 point2: classpathのデフォルト位置
 * https://teratail.com/questions/83170
 */

@EnableAutoConfiguration
@ImportResource("classpath:springmvc-config.xml")
@PropertySources({ @PropertySource("classpath:directory.properties"), @PropertySource("classpath:csv.properties") })
public class OjtApplication2 extends InitCheckFile {
	private static final Logger log = LoggerFactory.getLogger(OjtApplication2.class);
	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(OjtApplication2.class);
		application.setWebEnvironment(false);
		ApplicationContext context = application.run(args);
		SpringApplication.exit(context);

	}

	public void runDetails() {
		/*
		 * point1: 前処理(init)と実処理(exec)のように処理を分割(オブジェクト化)。 point2:if文の判定に処理を記載する。point3:
		 * 変則的なif文記載方法についての例文。
		 */
		baseDir = env.getProperty("moveDir");
		opponentDir = env.getProperty("outputDir");

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
	InputService execService;

	public Boolean exec(File[] files) {
		/*
		 * point1:命名規則にisSuccessのような実行結果を表す変数名を作成する 。
		 */
		Boolean isSuccess = false;
		isSuccess = execService.execDetails(files);
		return isSuccess;
	}

}
