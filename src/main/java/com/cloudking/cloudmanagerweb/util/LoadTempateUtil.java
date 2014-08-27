/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd.
 * All rights reserved.
 * Created on  Oct 17, 2012  2:17:57 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.apache.commons.io.FileUtils;

import com.cloudking.cloudmanagerweb.entity.TemplateEntity;

/**
 * 上传或则下载模版的工具类
 * 
 * @author CloudKing
 */
public final class LoadTempateUtil {
    /**
     * 线程池
     */
    private static ExecutorService executorService = Executors
                    .newFixedThreadPool(4);

    /**
     * 默认构造方法
     */
    private LoadTempateUtil(){

    }

    /**
     * 下载模版
     * 
     * @throws IOException
     *             文件存在会抛出此异常。
     * @return
     */
    public static void downloadTmplate(Long templateID, String domainCode,
                    String templateName, String url) throws IOException {
        File domainTmplateDir = new File(ProjectUtil.getTemplateDir(),
                        domainCode);
        if (!domainTmplateDir.exists()) {
            if (!domainTmplateDir.mkdirs()) {
                throw new IOException("创建模板目录失败");
            }
        }
        File templateFile = new File(domainTmplateDir, templateName);
        if (templateFile.exists()) {
            throw new IOException("文件已经存在");
        }
        URL urlReal = null;
        try {
            urlReal = new URL(url);
            urlReal.openConnection().connect();
        } catch (ConnectException e) {
            throw new IOException(String.format("连接[%1$s]失败", url));
        } catch (MalformedURLException e) {
            throw new IOException(String.format("URL[%1$s]格式错误", url));
        }
        DownExecutor downExecutor = new DownExecutor();
        downExecutor.setUrl(urlReal);
        downExecutor.setTemplatefile(templateFile);
        downExecutor.setTemplateID(templateID);
        executorService.execute(downExecutor);
    }

    /**
     * 下载执行器
     * 
     * @author CloudKing
     */
    private static class DownExecutor implements Runnable {
        /**
         * URL地址
         */
        private URL url;
        /**
         * 模板名字
         */
        private File templatefile;
        /**
         * 模版ID
         */
        private Long templateID;

        /**
         * @return the templateID
         */
        public Long getTemplateID() {
            return templateID;
        }

        /**
         * @param templateID
         *            the templateID to set
         */
        public void setTemplateID(Long templateID) {
            this.templateID = templateID;
        }

        /**
         * 
         */
        @Override
        public void run() {
            TemplateEntity templateEntity = null;
            EntityManager em = null;
            EntityTransaction transaction = null;
            try {
                FileUtils.copyURLToFile(url, templatefile);
                //修改当前模版状态
                em = ProjectUtil.getEntityManager();
                transaction = em.getTransaction();
                transaction.begin();
                templateEntity = em.find(TemplateEntity.class, templateID);
                if (templateEntity != null) {
                    templateEntity.setStatus(Constant.TEMPLATE_STATUS_OK);
                    em.merge(templateEntity);
                } else {
                    templatefile.delete();
                }
            } catch (IOException e) {
                LogUtil.error(e);
                if (templateEntity != null) {
                    templateEntity.setStatus(Constant.TEMPLATE_STATUS_DOWNLOAD_ERROR);
                }
            } finally {
                if (transaction != null) {
                    transaction.commit();
                }
                if (em != null) {
                    em.close();
                }
            }
        }

        /**
         * @return the url
         */
        public URL getUrl() {
            return url;
        }

        /**
         * @param url
         *            the url to set
         */
        public void setUrl(URL url) {
            this.url = url;
        }

        /**
         * @return the templatefile
         */
        public File getTemplatefile() {
            return templatefile;
        }

        /**
         * @param templatefile
         *            the templatefile to set
         */
        public void setTemplatefile(File templatefile) {
            this.templatefile = templatefile;
        }

    }
}
