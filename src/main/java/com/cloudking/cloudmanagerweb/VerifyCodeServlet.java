/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cloudking.cloudmanagerweb.util.Constant;

/**
 * 生成验证码的servlet
 * 
 * @author CloudKing
 * 
 */
public class VerifyCodeServlet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 187486890606131383L;

    /**
     * @throws ServletException
     *             servlet异常
     * @throws IOException
     *             IO异常
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @throws ServletException
     *              servlet异常
     * @throws IOException 
     *             IO异常
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ByteArrayInputStream baIs = getImage(request.getSession());
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = baIs.read(bytes)) != -1) {
            response.getOutputStream().write(bytes, 0, len);
        }
        baIs.close();
        response.setContentType("image/jpeg");
    }

    /**
     * 获取图片
     * 
     * @param session
     * @return
     */
    private ByteArrayInputStream getImage(HttpSession session) {
        // 在内存中创建图象
        final int width = 60;
        final int height = 20;
        final int randColor1 = 200;
        final int randColor2 = 250;
        final int fontSize = 18;
        final int randColor3 = 160;
        final int randColor4 = 200;
        final int lineCount = 155;
        final int lineRandomRange = 12;
        final int sRandCount = 4;
        final int r1 = 110;
        final int r2 = 13;
        final int r3 = 6;
        final int r4 = 16;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(randColor1, randColor2));
        g.fillRect(0, 0, width, height);
        // 设定字体
        g.setFont(new Font("Times New Roman", Font.PLAIN, fontSize));
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(randColor3, randColor4));
        for (int i = 0; i < lineCount; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(lineRandomRange);
            int yl = random.nextInt(lineRandomRange);
            g.drawLine(x, y, x + xl, y + yl);
        }
        // 取随机产生的认证码(6位数字)
        StringBuffer sRand = new StringBuffer();
        for (int i = 0; i < sRandCount; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(r1), 20 + random.nextInt(r1), 20 + random.nextInt(r1)));
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
            g.drawString(rand, r2 * i + r3, r4);
        }
        // 赋值验证码
        session.setAttribute(Constant.VERIFY_CODE, sRand.toString());

        // 图象生效
        g.dispose();
        ByteArrayInputStream input = null;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
            ImageIO.write(image, "JPEG", imageOut);
            imageOut.close();
            input = new ByteArrayInputStream(output.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;/* 赋值图像 */
    }

    /**
     * 给定范围获得随机颜色
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        final int color = 255;
        if (fc > color) {
            fc = color;
        }
        if (bc > color) {
            bc = color;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}
