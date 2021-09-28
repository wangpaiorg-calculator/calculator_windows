package org.wangpai.calculator.service;

import org.wangpai.calculator.controller.MiddleController;
import org.wangpai.calculator.controller.TerminalController;
import org.wangpai.calculator.controller.Url;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @since 2021-8-1
 */
@Lazy
@Scope("singleton")
@Controller("computingCenter")
public class ComputingCenter implements TerminalController, MiddleController {
    @Qualifier("dispatcher")
    @Autowired
    private MiddleController upperController;

    @Qualifier("calculatorService")
    @Autowired
    private CalculatorService calculatorService;

    @Override
    public void passUp(Url url, Object data, MiddleController lowerController) {
        upperController.passUp(url, data, this);
    }

    /**
     * 此方法一定先被调用
     */
    @Override
    public void passDown(Url url, Object data, MiddleController upperController) {
        receive(url, data); // 注意：此处不使用 url.generateLowerUrl()
    }

    @Override
    public void send(Url url, Object data) {
        passUp(url, data, null);
    }

    @Override
    public void receive(Url url, Object data) {
        if (data instanceof String) {
            this.receive(url, (String) data);
        } else {
            // 敬请期待
        }
    }

    private void receive(Url url, String str) {
        switch (url.getFirstLevelDirectory()) {
            case "expression":
                calculatorService.readExpression(str);
                break;

            default:
                break;
        }
    }
}
