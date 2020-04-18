package com.ft.gmall.user.service;

import com.ft.gmall.user.bean.UmsMember;
import com.ft.gmall.user.mapper.UmsMemberMapper;
import com.ft.gmall.util.RedisUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {
    @Autowired
    UmsMemberMapper umsMemberMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public List<UmsMember> getAllUsers() {
        List<UmsMember> umsMembers = umsMemberMapper.selectAll();
        return umsMembers;
    }

    @Override
    public UmsMember getUserById(Integer userid) {
        Example example = new Example(UmsMember.class);
        example.createCriteria().andEqualTo("id", userid);
        UmsMember umsMember = umsMemberMapper.selectOneByExample(example);
        return umsMember;
    }

    @Override
    public int addUser(UmsMember umsMember) {
        int result = umsMemberMapper.insert(umsMember);
        return result;
    }

    @Override
    public int updateUser(UmsMember umsMember) {
//        int result = umsMemberMapper.updateByPrimaryKey(umsMember);
//        System.out.println("##############################################");
//        System.out.println("##############################################");
//        System.out.println("##############################################");
//        Example example = new Example(UmsMember.class);
//        Example.Criteria criteria = example.createCriteria().andEqualTo("id", umsMember.getId());
//        UmsMember umsMember1 = umsMemberMapper.selectOneByExample(example);
//        System.out.println(umsMember1);
//        criteria.andAllEqualTo(umsMember);
//        umsMemberMapper.updateByExample(umsMember,example);
//        return result;
        return 0;
    }

    @Override
    public int delectUserById(Integer userId) {
        Example example = new Example(UmsMember.class);
        example.createCriteria().andEqualTo("id",userId);
        int result = umsMemberMapper.deleteByExample(example);
        return result;
    }

    @Override
    public UmsMember getUserByName(String loginName) {
        Example example = new Example(UmsMember.class);
        example.createCriteria().andEqualTo("userName", loginName);
        UmsMember umsMember = umsMemberMapper.selectOneByExample(example);
        return umsMember;
    }

    @Override
    public UmsMember login(UmsMember userInfo) {
        Example example = new Example(UmsMember.class);
        example.createCriteria().andEqualTo("userName", userInfo.getUserName());
        UmsMember umsMember = umsMemberMapper.selectOneByExample(example);
        if(umsMember.getPassword().equals(userInfo.getPassword()))
            return umsMember;
        return null;
    }

    @Override
    public void addUserToken(String token, String userId) {
        Jedis jedis = redisUtil.getJedis();
        jedis.setex("user:"+userId+":token", 60*3, token);
        jedis.close();
    }
}
