// --- File: src/main/java/com/example/survey/mapper/UserMapper.java ---
package com.example.survey.mapper;

import com.example.survey.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 用户表的数据库操作接口。
 * 所有SQL实现均定义在 "resources/com/example/survey/mapper/UserMapper.xml" 中。
 */
@Mapper
public interface UserMapper {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 匹配的用户对象，如果不存在则返回null
     */
    User findByUsername(String username);

    /**
     * 插入一个新用户
     * @param user 待插入的用户对象
     */
    void insert(User user);

    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<User> findAll();

    /**
     * 统计所有用户总数
     * @return 用户总数
     */
    long countAll();

    /**
     * 根据用户ID更新其状态
     * @param id 用户ID
     * @param status 新的状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新用户的个人资料 (邮箱和头像)
     * @param user 包含更新后信息的User对象
     * @return 影响的行数
     */
    int updateProfile(User user);

    /**
     * 更新用户的密码
     * @param id 用户ID
     * @param password 加密后的新密码
     * @return 影响的行数
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 更新用户的用户名
     * @param id 用户ID
     * @param username 新的用户名
     * @return 影响的行数
     */
    int updateUsername(@Param("id") Long id, @Param("username") String username);

    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 用户对象
     */
    User findById(Long id);
}

