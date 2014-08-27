/**
 * 
 */
package com.cloudking.cloudmanagerweb.service.machineroom;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.cloudking.cloudmanagerweb.BaseService;
import com.cloudking.cloudmanagerweb.CloudContext;
import com.cloudking.cloudmanagerweb.dao.MachineRackDAO;
import com.cloudking.cloudmanagerweb.dao.MachineRoomDAO;
import com.cloudking.cloudmanagerweb.entity.MachineRoomEntity;
import com.cloudking.cloudmanagerweb.util.ProjectUtil;
import com.cloudking.cloudmanagerweb.vo.MachineRoomVO;

/**
 * 机房service
 * 
 * @author CloudKing
 */
@SuppressWarnings("unchecked")
@Service("machineRoomService")
public class MachineRoomService extends BaseService {

    /**
     * 机房DAO
     */
    @Resource
    private MachineRoomDAO machineRoomDAO;

    /**
     * 机架DAO
     */
    @Resource
    private MachineRackDAO machineRackDAO;

    /**
     * 搜索机房
     * 
     * @exception Exception
     *                抛出所有异常
     */
    public void query(CloudContext<MachineRoomVO> cloudContext) throws Exception {
        //结果集
        List<MachineRoomVO> queryResult = new ArrayList<MachineRoomVO>();
        //总数据数
        cloudContext.getPageInfo().setDataCount(machineRoomDAO.getQueryCount(cloudContext.getStringParam("qName")));
        //查询数据
        if (cloudContext.getPageInfo().getDataCount() > 0) {
            List<MachineRoomEntity> machineRoomEntitys = machineRoomDAO.query(cloudContext.getStringParam("qName"),
                            cloudContext.getPageInfo());
            MachineRoomVO machineRoomVO = null;
            for (MachineRoomEntity machineRoomEntity : machineRoomEntitys) {
                machineRoomVO = new MachineRoomVO();
                BeanUtils.copyProperties(machineRoomEntity, machineRoomVO);
                queryResult.add(machineRoomVO);
            }
        }
        cloudContext.addParam("machineRooms", queryResult);
    }

    /**
     * 新增
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void insert(CloudContext<MachineRoomVO> cloudContext) throws Exception {
        //验证
        MachineRoomEntity machineRoomEntity = machineRoomDAO.getByName(cloudContext.getVo().getName());
        if (machineRoomEntity != null) {
            cloudContext.addErrorMsg(String.format("【%1$s】已经存在", cloudContext.getVo().getName()));
            return;
        }
        machineRoomEntity = new MachineRoomEntity();
        BeanUtils.copyProperties(cloudContext.getVo(), machineRoomEntity);
        machineRoomEntity.setAddTime(new Date());
        machineRoomEntity.setIdentityName(ProjectUtil.createMachineRoomIdentityName());

        /**
         * 添加之后的操作
         */
        File dir = new File(ProjectUtil.getRRDDir(), machineRoomEntity.getIdentityName());
        if (dir.exists()) {
            //存在，并且不是目录，就删除，再添加
            if (!dir.isDirectory()) {
                if (dir.delete()) {
                    dir.mkdirs();
                }
            }
        } else {
            dir.mkdirs();
        }
        //提示信息
        machineRoomDAO.insert(machineRoomEntity);
        cloudContext.addSuccessMsg("添加成功!");
    }

    /**
     * 修改
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void update(CloudContext<MachineRoomVO> cloudContext) throws Exception {
        MachineRoomEntity machineRoomEntity = machineRoomDAO.get(cloudContext.getVo().getId());
        if (machineRoomEntity == null) {
            cloudContext.addErrorMsg("机房不存在");
            return;
        }
        machineRoomEntity.setDesc(cloudContext.getVo().getDesc());
        machineRoomDAO.update(machineRoomEntity);
        //提示信息
        cloudContext.addSuccessMsg("修改成功!");
    }

    /**
     * 删除
     * 
     * @return
     * @throws Exception
     *             抛出所有异常
     */
    public void delete(CloudContext<MachineRoomVO> cloudContext) throws Exception {
        MachineRoomEntity machineRoomEntity = machineRoomDAO.get(cloudContext.getVo().getId());
        if (machineRoomEntity == null) {
            cloudContext.addErrorMsg("机房不存在，请刷新后重试");
            return;
        }
        if (machineRackDAO.getCountByMachineRoom(machineRoomEntity.getId()) > 0) {
            cloudContext.addErrorMsg("删除失败，请先删除下面已存在的机架！");
            return;
        }
        /**
         * 删除之后的操作
         */
        File dir = new File(ProjectUtil.getRRDDir(), machineRoomEntity.getIdentityName());
        if (dir.exists()) {
            dir.delete();
        }
        machineRoomDAO.delete(machineRoomEntity);
        //提示信息
        cloudContext.addSuccessMsg("删除成功!");

    }

    /**
     * 新增或修改
     * 
     * @param cloudContext
     * @throws Exception
     *             所有异常
     */
    public void initAddOrUpdate(CloudContext<MachineRoomVO> cloudContext) throws Exception {
        //如果是修改，就加载属性
        if (cloudContext.getBooleanParam("updateFlag")) {
            MachineRoomEntity machineRoomEntity = machineRoomDAO.get(cloudContext.getVo().getId());
            if (machineRoomEntity == null) {
                cloudContext.addErrorMsg("机房不存在！");
                return;
            }
            MachineRoomVO machineRoomVO = new MachineRoomVO();
            BeanUtils.copyProperties(machineRoomEntity, machineRoomVO);
            cloudContext.addParam("dataVo", machineRoomVO);
        }

    }
}
