package org.ff.service.impl;

import org.ff.service.FunctionService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("functionService")
public class FunctionServiceImpl implements FunctionService {

    @Override
    public Set<String> getFunctionCodeSet(Set<String> roleCodes) {
        return null;
    }
}
