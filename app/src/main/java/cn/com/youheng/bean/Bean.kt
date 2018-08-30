package cn.com.youheng.bean

/**
 * Created by laplace on 2018/8/29.
 */
data class City(var id: Int, var name: String, var parent_id: Int)


data class Result<P>(var code: Int, var data: P, var msg: String?)


data class User(var name: String)