package com.activity.greendao;


import com.activity.FrpAndroid;

import java.util.List;

public class DBSuiDaoHelper {
    public static void insertsuidao(SuiDao suidao) {
        FrpAndroid.getDaoInstant().getSuiDaoDao().insertOrReplace(suidao);
    }

    /**
     * 删除数据
     *
     * @param id
     */
    public static void deletesuidao(long id) {
        FrpAndroid.getDaoInstant().getSuiDaoDao().deleteByKey(id);
    }

    public static void deleteALL() {
        FrpAndroid.getDaoInstant().getSuiDaoDao().deleteAll();
    }

    /**
     * 更新数据
     */
    public static void updatesuidao(SuiDao shop) {
        FrpAndroid.getDaoInstant().getSuiDaoDao().update(shop);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public static List<SuiDao> queryAll() {
        return FrpAndroid.getDaoInstant().getSuiDaoDao().loadAll();
    }

    public static boolean haveSameSetionName(String uid) {
        return FrpAndroid.getDaoInstant().getSuiDaoDao().queryBuilder().where(SuiDaoDao.Properties.Name.eq(uid)).list().size() > 0;
    }

}
