/**
 * Copyright(c) 2012 ShenZhen CloudKing Technology Co., Ltd
 * All rights reserved.
 * Created on Oct 15, 2012  2:17:56 PM
 */
package com.cloudking.cloudmanagerweb.util;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphConstants;
import org.rrd4j.graph.RrdGraphDef;

import com.cloudking.cloudmanager.core.backup.BackupStorage;
import com.cloudking.cloudmanager.core.compute.Compute;
import com.cloudking.cloudmanager.core.exception.VirtualizationException;
import com.cloudking.cloudmanager.core.storage.Storage;
import com.cloudking.cloudmanager.core.util.CapacityUnit;

/**
 * RRD工具类
 * 
 * @author CloudKing
 */
public final class RRDUtil {

    /**
     * CPU类型 user
     */
    public static final String CPU_USER_TYPE = "cpu_user";
    /**
     * CPU类型 system
     */
    public static final String CPU_SYSTEM_TYPE = "cpu_sys";
    /**
     * CPU类型 idle
     */
    public static final String CPU_IDLE_TYPE = "cpu_idle";
    /**
     * CPU类型 iowait
     */
    public static final String CPU_IOWAIT_TYPE = "cpu_iowait";
    /**
     * 内存类型 total
     */
    public static final String MEM_TOTAL_TYPE = "mem_total";
    /**
     * 内存类型 free
     */
    public static final String MEM_FREE_TYPE = "mem_free";
    /**
     * 内存类型 used
     */
    public static final String MEM_USED_TYPE = "mem_used";

    /**
     * 硬盘类型 used
     */
    public static final String DISK_USED_TYPE = "disk_used";

    /**
     * 内存RRDS
     */
    private static final String MEMORY_RRD = "_memory.rrd";
    /**
     * CPU的RRDS
     */
    private static final String CPU_RRD = "_cpu.rrd";
    /**
     * 硬盘的RRDS
     */
    private static final String DISK_RRD = "_disk.rrd";

    /**
     * RRD图片宽度
     */
    private static final Integer IMG_WIDTH = 500;

    /**
     * RRD图片高度
     */
    private static final Integer IMG_HEIGHT = 200;

    /**
     * 默认构造方法
     */
    private RRDUtil(){

    }

    /**
     * 创建计算资源rrd数据库文件
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @throws IOException
     *             IO异常
     * @return
     */
    public static void createComputeResourceRRDS(String pathFlag) throws IOException {
        File rrdCPUFile = new File(pathFlag + CPU_RRD);
        createCPURRDS(rrdCPUFile);
        File rrdMemoryFile = new File(pathFlag + MEMORY_RRD);
        createMemoryRRDS(rrdMemoryFile);
    }

    /**
     * 创建存储资源rrd数据库文件
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @throws IOException
     *             IO异常
     * @return
     */
    public static void createStorageResourceRRDS(String pathFlag) throws IOException {
        File rrdDiskFile = new File(pathFlag + DISK_RRD);
        createDiskRRDS(rrdDiskFile);
    }

    /**
     * 移动rrd数据库文件
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @return
     * @throws IOException
     *             io异常
     */
    public static void moveRRDFile(final String srcPath, String destPath) throws IOException {
        if (srcPath.equals(destPath)) {
            return;
        }
        String parentSrcPath = srcPath.substring(0, srcPath.lastIndexOf(File.separator));
        File parentSrcDir = new File(parentSrcPath);
        File[] filelist = parentSrcDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getAbsolutePath().matches("^" + srcPath + ".*$")) {
                    return true;
                }
                return false;
            }
        });
        String path = null;
        File destFile = null;
        for (File f : filelist) {
            path = f.getAbsolutePath();
            destFile = new File(path.replace(srcPath, destPath));
            FileUtils.moveFile(f, destFile);
        }
    }

    /**
     * 移动rrd文件夹
     * 
     * @param srcPath
     * @param destPath
     * @throws IOException
     *             io异常
     */
    public static void moveRRDDir(String srcPath, String destPath) throws IOException {
        if (!srcPath.equals(destPath)) {
            FileUtils.moveDirectory(new File(srcPath), new File(destPath));
        }
    }

    /**
     * 创建备份存储资源rrd数据库文件
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @throws IOException
     *             IO异常
     * @return
     */
    public static void createBackupStorageResourceRRDS(String pathFlag) throws IOException {
        File rrdDiskFile = new File(pathFlag + DISK_RRD);
        createDiskRRDS(rrdDiskFile);
    }

    /**
     * 删除计算资源rrd数据库文件
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @return
     */
    public static synchronized void deleteComputeResourceRRDS(String pathFlag) {
        File rrdCPUFile = new File(pathFlag + CPU_RRD);
        if (rrdCPUFile.exists()) {
            rrdCPUFile.delete();
        }
        File rrdMemFile = new File(pathFlag + MEMORY_RRD);
        if (rrdMemFile.exists()) {
            rrdMemFile.delete();
        }
    }

    /**
     * 删除存储rrd数据库文件
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @return
     */
    public static synchronized void deleteStorageResourceRRDS(String pathFlag) {
        File rrdDiskFile = new File(pathFlag + DISK_RRD);
        if (rrdDiskFile.exists()) {
            rrdDiskFile.delete();
        }
    }

    /**
     * 删除备份存储rrd数据库文件
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @return
     */
    public static synchronized void deleteBackupStorageResourceRRDS(String pathFlag) {
        File rrdDiskFile = new File(pathFlag + DISK_RRD);
        if (rrdDiskFile.exists()) {
            rrdDiskFile.delete();
        }
    }

    /**
     * 插入计算节点数据
     * 
     * @param compute
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @throws IOException
     *             插入数据
     */
    public static synchronized void insertComputeResourceData(Compute compute, String pathFlag) throws IOException {
        //准备文件
        File rrdCPUFile = new File(pathFlag + CPU_RRD);
        if (!rrdCPUFile.exists()) {
            return;
        }
        File rrdMemoryFile = new File(pathFlag + MEMORY_RRD);
        if (!rrdMemoryFile.exists()) {
            return;
        }

        Long dateTime = System.currentTimeMillis() / 1000L;
        /*
         * CPU
         */

        long cpuResTotal = compute.getUserCpu() + compute.getSystemCpu() + compute.getIdleCpu()
                        + compute.getIowaitCpu();
        Double userCpu = getPercent(compute.getUserCpu() / (double) cpuResTotal).doubleValue();
        Double systemCpu = getPercent(compute.getSystemCpu() / (double) cpuResTotal).doubleValue();
        Double idleCpu = getPercent(compute.getIdleCpu() / (double) cpuResTotal).doubleValue();
        Double iowaitCpu = getPercent(compute.getIowaitCpu() / (double) cpuResTotal).doubleValue();

        RrdDb rrdCPUDb = new RrdDb(rrdCPUFile.getAbsolutePath());

        //如果时间小于开始时间或者最后更新时间，就返回
        if (dateTime <= rrdCPUDb.getRrdDef().getStartTime()) {
            return;
        }
        if (dateTime <= rrdCPUDb.getLastUpdateTime()) {
            return;
        }

        Sample cpuSample = rrdCPUDb.createSample();
        cpuSample.setTime(dateTime);
        cpuSample.setValue(CPU_USER_TYPE, userCpu);
        cpuSample.setValue(CPU_SYSTEM_TYPE, systemCpu);
        cpuSample.setValue(CPU_IDLE_TYPE, idleCpu);
        cpuSample.setValue(CPU_IOWAIT_TYPE, iowaitCpu);
        cpuSample.update();
        rrdCPUDb.close();

        /*
         * 内存
         */

        //MB
        Double totalMem = getPercent(compute.getTotalMem() / 1024.0).doubleValue();
        Double freeMem = getPercent(compute.getFreeMem() / 1024.0).doubleValue();
        Double usedMem = totalMem - freeMem;

        RrdDb rrdMemDb = new RrdDb(rrdMemoryFile.getAbsolutePath());
        Sample memSample = rrdMemDb.createSample();
        memSample.setTime(dateTime);
        memSample.setValue(MEM_TOTAL_TYPE, totalMem);
        memSample.setValue(MEM_FREE_TYPE, freeMem);
        memSample.setValue(MEM_USED_TYPE, usedMem);
        memSample.update();
        rrdMemDb.close();
    }

    /**
     * 插入存储节点数据
     * 
     * @param nodeStat
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @throws IOException
     *             插入数据
     */
    public static synchronized void insertStorageResourceData(Storage storage, String pathFlag) throws IOException {
        /*
         * DIS
         */
        File rrdDiskFile = new File(pathFlag + DISK_RRD);
        if (!rrdDiskFile.exists()) {
            return;
        }
        Long dateTime = System.currentTimeMillis() / 1000L;
        //GB
        storage.setUnit(CapacityUnit.GB);
        long available;
        long capacity;
        try {
            available = storage.getAvailable();
            capacity = storage.getCapacity();
        } catch (VirtualizationException e) {
            throw new IOException(e);
        }
        long used = capacity - available;
        RrdDb rrdCPUDb = new RrdDb(rrdDiskFile.getAbsolutePath());
        //如果时间小于开始时间或者最后更新时间，就返回
        if (dateTime <= rrdCPUDb.getRrdDef().getStartTime()) {
            return;
        }
        if (dateTime <= rrdCPUDb.getLastUpdateTime()) {
            return;
        }
        Sample storageSample = rrdCPUDb.createSample();
        storageSample.setTime(dateTime);
        storageSample.setValue(DISK_USED_TYPE, used);
        storageSample.update();
        rrdCPUDb.close();
    }

    /**
     * CPU画图
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @throws IOException
     *             IO异常
     * @return
     */
    public static byte[] graphCPU(String pathFlag, String cpuType, String title, Date startDate, Date endDate)
                    throws IOException {
        File rrdFile = new File(pathFlag + CPU_RRD);
        if (!rrdFile.exists()) {
            return null;
        }
        RrdGraphDef graphDef = new RrdGraphDef();
        //设置字体
        graphDef.setLargeFont(new Font("宋体", Font.BOLD, 14));
        graphDef.setSmallFont(new Font("宋体", Font.PLAIN, 12));
        //高度和宽度
        graphDef.setWidth(IMG_WIDTH);
        graphDef.setHeight(IMG_HEIGHT);
        //修改箭头颜色
        graphDef.setColor(RrdGraphConstants.COLOR_ARROW, Color.BLACK);
        //设置度量,0为不使用度量
        graphDef.setBase(0.0);
        //标题
        graphDef.setTitle(title);
        graphDef.setVerticalLabel("单位(%)");
        graphDef.setTimeSpan(startDate.getTime() / 1000L, endDate.getTime() / 1000L);
        //用户使用率
        if (CPU_USER_TYPE.equals(cpuType)) {
            graphDef.datasource("avg", rrdFile.getAbsolutePath(), cpuType, ConsolFun.AVERAGE);
            graphDef.line("avg", Color.GREEN, "用户使用率");
            graphDef.gprint("avg", ConsolFun.AVERAGE, "%6.1lf 平均值");
            graphDef.gprint("avg", ConsolFun.MIN, "%6.1lf 最小值");
            graphDef.gprint("avg", ConsolFun.MAX, "%6.1lf 最大值");
        } else if (CPU_IDLE_TYPE.equals(cpuType)) {
            graphDef.datasource("avg", rrdFile.getAbsolutePath(), cpuType, ConsolFun.AVERAGE);
            graphDef.line("avg", Color.GREEN, "空闲率");
            graphDef.gprint("avg", ConsolFun.AVERAGE, "%6.1lf 平均值");
            graphDef.gprint("avg", ConsolFun.MIN, "%6.1lf 最小值");
            graphDef.gprint("avg", ConsolFun.MAX, "%6.1lf 最大值");
        } else if (CPU_IOWAIT_TYPE.equals(cpuType)) {
            graphDef.datasource("avg", rrdFile.getAbsolutePath(), cpuType, ConsolFun.AVERAGE);
            graphDef.line("avg", Color.GREEN, "IO使用率");
            graphDef.gprint("avg", ConsolFun.AVERAGE, "%6.1lf 平均值");
            graphDef.gprint("avg", ConsolFun.MIN, "%6.1lf 最小值");
            graphDef.gprint("avg", ConsolFun.MAX, "%6.1lf 最大值");
        } else if (CPU_SYSTEM_TYPE.equals(cpuType)) {
            graphDef.datasource("avg", rrdFile.getAbsolutePath(), cpuType, ConsolFun.AVERAGE);
            graphDef.line("avg", Color.GREEN, "系统使用率");
            graphDef.gprint("avg", ConsolFun.AVERAGE, "%6.1lf 平均值");
            graphDef.gprint("avg", ConsolFun.MIN, "%6.1lf 最小值");
            graphDef.gprint("avg", ConsolFun.MAX, "%6.1lf 最大值");
        } else {
            throw new IllegalArgumentException(String.format("cpuType【%1$s】不正确", cpuType));
        }
        //返回图片字节流
        RrdGraph graph = new RrdGraph(graphDef);
        return graph.getRrdGraphInfo().getBytes();
    }

    /**
     * 内存
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @throws IOException
     *             IO异常
     * @return
     */
    public static byte[] graphMem(String pathFlag, String memType, String title, Date startDate, Date endDate)
                    throws IOException {
        File rrdFile = new File(pathFlag + MEMORY_RRD);
        if (!rrdFile.exists()) {
            return null;
        }
        RrdGraphDef graphDef = new RrdGraphDef();
        //设置字体
        graphDef.setLargeFont(new Font("宋体", Font.BOLD, 14));
        graphDef.setSmallFont(new Font("宋体", Font.PLAIN, 12));
        //高度和宽度
        graphDef.setWidth(IMG_WIDTH);
        graphDef.setHeight(IMG_HEIGHT);
        //设置度量,0为不使用度量
        graphDef.setBase(0.0);
        //修改箭头颜色
        graphDef.setColor(RrdGraphConstants.COLOR_ARROW, Color.BLACK);
        //标题
        graphDef.setTitle(title);
        graphDef.setVerticalLabel("单位(MB)");
        graphDef.setTimeSpan(startDate.getTime() / 1000L, endDate.getTime() / 1000L);
        //用户使用率
        if (MEM_TOTAL_TYPE.equals(memType)) {
            graphDef.datasource("avg", rrdFile.getAbsolutePath(), memType, ConsolFun.AVERAGE);
            graphDef.line("avg", Color.GREEN, "总使用情况");
            graphDef.gprint("avg", ConsolFun.AVERAGE, "%6.1lf 平均值");
            graphDef.gprint("avg", ConsolFun.MIN, "%6.1lf 最小值");
            graphDef.gprint("avg", ConsolFun.MAX, "%6.1lf 最大值");
        } else if (MEM_FREE_TYPE.equals(memType)) {
            graphDef.datasource("avg", rrdFile.getAbsolutePath(), memType, ConsolFun.AVERAGE);
            graphDef.line("avg", Color.GREEN, "剩余情况");
            graphDef.gprint("avg", ConsolFun.AVERAGE, "%6.1lf 平均值");
            graphDef.gprint("avg", ConsolFun.MIN, "%6.1lf 最小值");
            graphDef.gprint("avg", ConsolFun.MAX, "%6.1lf 最大值");
        } else if (MEM_USED_TYPE.equals(memType)) {
            graphDef.datasource("avg", rrdFile.getAbsolutePath(), memType, ConsolFun.AVERAGE);
            graphDef.line("avg", Color.GREEN, "已使用情况");
            graphDef.gprint("avg", ConsolFun.AVERAGE, "%6.1lf 平均值");
            graphDef.gprint("avg", ConsolFun.MIN, "%6.1lf 最小值");
            graphDef.gprint("avg", ConsolFun.MAX, "%6.1lf 最大值");
        } else {
            throw new IllegalArgumentException(String.format("memType【%1$s】不正确", memType));
        }
        //返回图片字节流
        RrdGraph graph = new RrdGraph(graphDef);
        return graph.getRrdGraphInfo().getBytes();
    }

    /**
     * 硬盘
     * 
     * @param equipIdentityName
     * @param machineRackIdentityName
     * @param machineRoomIdentityName
     * @throws IOException
     *             IO异常
     * @return
     */
    public static byte[] graphDisk(String pathFlag, String diskType, long capacily, String title, Date startDate,
                    Date endDate) throws IOException {
        File rrdFile = new File(pathFlag + DISK_RRD);
        if (!rrdFile.exists()) {
            return null;
        }
        RrdGraphDef graphDef = new RrdGraphDef();
        //设置字体
        graphDef.setLargeFont(new Font("宋体", Font.BOLD, 14));
        graphDef.setSmallFont(new Font("宋体", Font.PLAIN, 12));
        //高度和宽度
        graphDef.setWidth(IMG_WIDTH);
        graphDef.setHeight(IMG_HEIGHT);
        //设置度量,0为不使用度量
        graphDef.setBase(0.0);
        //修改箭头颜色
        graphDef.setColor(RrdGraphConstants.COLOR_ARROW, Color.BLACK);
        //标题
        graphDef.setTitle(title);
        graphDef.setVerticalLabel("单位(GB)");
        graphDef.setTimeSpan(startDate.getTime() / 1000L, endDate.getTime() / 1000L);
        //用户使用率
        if (DISK_USED_TYPE.equals(diskType)) {
            graphDef.datasource("avg", rrdFile.getAbsolutePath(), diskType, ConsolFun.AVERAGE);
            graphDef.line("avg", Color.GREEN, "使用情况");
            graphDef.gprint("avg", ConsolFun.AVERAGE, "%6.1lf 平均值");
            graphDef.gprint("avg", ConsolFun.MIN, "%6.1lf 最小值");
            graphDef.gprint("avg", ConsolFun.MAX, "%6.1lf 最大值");
            graphDef.comment("\\n");
            graphDef.comment(String.format("总容量为%1$d", capacily));
        } else {
            throw new IllegalArgumentException(String.format("diskType【%1$s】不正确", diskType));
        }
        //返回图片字节流
        RrdGraph graph = new RrdGraph(graphDef);
        return graph.getRrdGraphInfo().getBytes();
    }

    /**
     * 获取小数点后四位数字(四去五进)
     * 
     * @param o
     * @return
     */
    private static BigDecimal getPercent(double o) {
        if (Double.isNaN(o)) {
            BigDecimal bd = new BigDecimal(0);
            return bd.setScale(4, BigDecimal.ROUND_HALF_UP);
        } else {
            BigDecimal bd = new BigDecimal(o * 100);
            return bd.setScale(4, BigDecimal.ROUND_HALF_UP);
        }
    }

    /**
     * 创建CPU RRD数据库
     * 
     * @param rrdFile
     * @throws IOException
     *             IO异常
     */
    private static void createCPURRDS(File rrdFile) throws IOException {
        RrdDef rrdDef = new RrdDef(rrdFile.getAbsolutePath());
        // 开始时间。一般都是创建rrd数据库的时间
        rrdDef.setStartTime(System.currentTimeMillis() / 1000L);
        // “期望” 每隔60秒获得一次数据
        rrdDef.setStep(60L);
        // 名字为user 原值 心跳600，没有最小值和最大值。
        rrdDef.addDatasource(CPU_USER_TYPE, DsType.GAUGE, 8460, Double.NaN, Double.NaN);
        // 名字为sys 原值 心跳600，没有最小值和最大值。
        rrdDef.addDatasource(CPU_SYSTEM_TYPE, DsType.GAUGE, 8460, Double.NaN, Double.NaN);
        // 名字为idle 原值 心跳600，没有最小值和最大值。
        rrdDef.addDatasource(CPU_IDLE_TYPE, DsType.GAUGE, 8460, Double.NaN, Double.NaN);
        // 名字为iowait 原值 心跳600，没有最小值和最大值。
        rrdDef.addDatasource(CPU_IOWAIT_TYPE, DsType.GAUGE, 8460, Double.NaN, Double.NaN);
        /*
         * 平均
         */
        // 4小时
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 240);
        //1天
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 5, 2880);
        //1星期
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 30, 4320);
        //1个月
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 360, 5840);
        // 创建数据库
        RrdDb rrdDb = new RrdDb(rrdDef);
        rrdDb.close();
    }

    /**
     * 创建CPU 内存数据库
     * 
     * @param rrdFile
     * @throws IOException
     *             IO异常
     */
    private static void createMemoryRRDS(File rrdFile) throws IOException {
        RrdDef rrdDef = new RrdDef(rrdFile.getAbsolutePath());
        // 开始时间。一般都是创建rrd数据库的时间
        rrdDef.setStartTime(System.currentTimeMillis() / 1000L);
        // “期望” 每隔60秒获得一次数据
        rrdDef.setStep(60L);
        // 名字为user 原值 心跳600，没有最小值和最大值。
        rrdDef.addDatasource(MEM_TOTAL_TYPE, DsType.GAUGE, 8460, Double.NaN, Double.NaN);
        // 名字为sys 原值 心跳600，没有最小值和最大值。
        rrdDef.addDatasource(MEM_FREE_TYPE, DsType.GAUGE, 8460, Double.NaN, Double.NaN);
        // 名字为idle 原值 心跳600，没有最小值和最大值。
        rrdDef.addDatasource(MEM_USED_TYPE, DsType.GAUGE, 8460, Double.NaN, Double.NaN);
        /*
         * 平均
         */
        // 4小时
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 240);
        //1天
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 5, 2880);
        //1星期
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 30, 4320);
        //1个月
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 360, 5840);
        // 创建数据库
        RrdDb rrdDb = new RrdDb(rrdDef);
        rrdDb.close();
    }

    /**
     * 创建硬盘数据库
     * 
     * @param rrdFile
     * @throws IOException
     *             IO异常
     */
    private static void createDiskRRDS(File rrdFile) throws IOException {
        RrdDef rrdDef = new RrdDef(rrdFile.getAbsolutePath());
        // 开始时间。一般都是创建rrd数据库的时间
        rrdDef.setStartTime(System.currentTimeMillis() / 1000L);
        // “期望” 每隔60秒获得一次数据
        rrdDef.setStep(60L);
        // 名字为user 原值 心跳600，没有最小值和最大值。
        rrdDef.addDatasource(DISK_USED_TYPE, DsType.GAUGE, 8460, Double.NaN, Double.NaN);
        /*
         * 平均
         */
        // 4小时
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 240);
        //1天
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 5, 2880);
        //1星期
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 30, 4320);
        //1个月
        rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 360, 5840);
        // 创建数据库
        RrdDb rrdDb = new RrdDb(rrdDef);
        rrdDb.close();
    }

    /**
     * * 插入备份存储节点数据
     * 
     * @param backupStorage
     * @param pathFlag
     * @throws IOException
     *             插入数据
     */
    public static void insertBackupStorageResourceData(BackupStorage backupStorage, String pathFlag) throws IOException {
        /*
         * DIS
         */
        File rrdDiskFile = new File(pathFlag + DISK_RRD);
        if (!rrdDiskFile.exists()) {
            return;
        }
        Long dateTime = System.currentTimeMillis() / 1000L;
        //GB
        long available;
        long capacity;
        try {
            available = ProjectUtil.kByteToGiga(backupStorage.getAvailable()).longValue();
            capacity = ProjectUtil.kByteToGiga(backupStorage.getCapacity()).longValue();
        } catch (Exception e) {
            throw new IOException(e);
        }
        long used = capacity - available;
        RrdDb rrdCPUDb = new RrdDb(rrdDiskFile.getAbsolutePath());
        //如果时间小于开始时间或者最后更新时间，就返回
        if (dateTime <= rrdCPUDb.getRrdDef().getStartTime()) {
            return;
        }
        if (dateTime <= rrdCPUDb.getLastUpdateTime()) {
            return;
        }
        Sample backupStorageSample = rrdCPUDb.createSample();
        backupStorageSample.setTime(dateTime);
        backupStorageSample.setValue(DISK_USED_TYPE, used);
        backupStorageSample.update();
        rrdCPUDb.close();
    }
}
