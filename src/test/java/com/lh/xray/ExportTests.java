package com.lh.xray;

import com.genz.xray.Xray;
import com.lh.runner.JunitRunner;
import com.lh.utilities.Configurations;

public class ExportTests {

	public static void main(String[] args) {

		export();

	}

	public static void export() {

		com.genz.config.PropertiesHandler.setConfigPath(Configurations.XrayConfigPath);

		JunitRunner.setLogger();

		Xray.exportCucumberTestsFromXray();

	}

}
