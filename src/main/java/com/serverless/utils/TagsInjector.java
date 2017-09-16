package com.serverless.utils;

import com.google.inject.AbstractModule;
import com.serverless.bl.dao.TagsDao;
import com.serverless.bl.dao.impl.TagsDaoImpl;
import com.serverless.bl.services.InputService;
import com.serverless.bl.services.TagsService;
import com.serverless.bl.services.impl.InputServiceImpl;
import com.serverless.bl.services.impl.TagsServiceImpl;

public class TagsInjector extends AbstractModule {

    @Override
    protected void configure() {
        bind(InputService.class).to(InputServiceImpl.class);
        bind(TagsService.class).to(TagsServiceImpl.class);
        bind(TagsDao.class).to(TagsDaoImpl.class);
    }

}
