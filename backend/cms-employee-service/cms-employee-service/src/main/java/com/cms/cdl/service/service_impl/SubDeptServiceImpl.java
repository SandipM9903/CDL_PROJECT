package com.cms.cdl.service.service_impl;

import com.cms.cdl.repo.SubDepartmentRepo;
import com.cms.cdl.service.service_interface.SubDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubDeptServiceImpl implements SubDeptService {
    @Autowired
    SubDepartmentRepo subDepartmentRepo;



}
