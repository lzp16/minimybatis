package com.lzp.dao;

import com.lzp.entity.User;

import java.util.List;

/**
 * Created by LiZhanPing on 2019/12/17.
 */
public interface UserMapper {

    User findById(Integer id);
}
