package com.example.base;

import java.io.File;

import org.springframework.boot.CommandLineRunner;

public abstract class BaseRun implements CommandLineRunner {
	protected String baseDir;
	protected String opponentDir;

	@Override
	public void run(String... args) {
		runDetails();
	}

	public abstract void runDetails();

	public abstract File[] init(String baseDir,String opponentDir);
}
