package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.ordervo.CourseWebVoOrder;
import com.atguigu.commonutils.ordervo.UcenterMemberOrder;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-02-05
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;


    //1.生成订单方法
    @Override
    public String createOrders(String courseId, String memberId) {
        //远程调用： 根据用户Id获取用户信息
        UcenterMemberOrder ucenterMemberOrder = ucenterClient.getUserInfoOrder(memberId);

        //通过调用： 根据课程id获取课程信息
        CourseWebVoOrder courseWebVoOrder = eduClient.getCourseInfoOrder(courseId);

        //创建订单，插入数据
        Order order = new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseWebVoOrder.getTitle());
        order.setCourseCover(courseWebVoOrder.getCover());
        order.setTeacherName(courseWebVoOrder.getTeacherName());
        order.setTotalFee(courseWebVoOrder.getPrice());
        order.setMemberId(memberId);
        order.setMobile(ucenterMemberOrder.getMobile());
        order.setNickname(ucenterMemberOrder.getNickname());
        order.setStatus(0);
        order.setPayType(1);
        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
