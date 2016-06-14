package com.config;

/*
 * To change this template, choose Tools | Templates 
 * and open the template in the editor.
 */
import javax.swing.SwingWorker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dao.UploadDao;
import com.dao.UploadDao2;

/**
 *
 * @author ScattLabs
 */
public class DataHolder extends SwingWorker<Void, Void> {

	private static DataHolder dataHolder;

	public static DataHolder getInstance() {
		if (dataHolder == null) {
			dataHolder = new DataHolder();
		}

		return dataHolder;
	}

	static ApplicationContext ctx;
	static UploadDao uploadDao;
	static UploadDao2 uploadDao2;

	public ApplicationContext getCtx() {
		if (ctx == null) {
			ctx = new AnnotationConfigApplicationContext(ApplicationConfig.class);
		}
		return ctx;
	}

	public UploadDao getUploadDao() {
		if (uploadDao == null) {
			uploadDao = (UploadDao) getCtx().getBean("uploadDaoImpl");
		}
		return uploadDao;
	}

	public UploadDao2 getUploadDao2() {
		if (uploadDao2 == null) {
			uploadDao2 = (UploadDao2) getCtx().getBean("uploadDaoImpl2");
		}
		return uploadDao2;
	}

	public void initializeData() {
		getCtx();
		getUploadDao();
		getUploadDao2();
	}

	@Override
	protected Void doInBackground() throws Exception {
		while (true) {
			initializeData();
			Thread.sleep(3600000);
		}
	}
};

;
