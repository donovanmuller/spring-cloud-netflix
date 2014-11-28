package org.springframework.cloud.netflix.zuul.sample;

import com.netflix.zuul.ZuulFilter;

public class SampleZuulFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        return null;
    }

}
