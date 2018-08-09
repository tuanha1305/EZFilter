package cn.ezandroid.ezfilter.demo.render;

import cn.ezandroid.ezfilter.core.FBORender;
import cn.ezandroid.ezfilter.multi.MultiInput;

/**
 * OverlayMultiInput
 *
 * @author like
 * @date 2018-08-09
 */
public class OverlayMultiInput extends MultiInput {

    public OverlayMultiInput() {
        super(2);
        setRenderSize(720, 1280); // video size
    }

    @Override
    public void registerFilter(FBORender filter) {
        filter.setRenderSize(getWidth(), getHeight());
        super.registerFilter(filter);
    }

    @Override
    protected String getFragmentShader() {
        return "precision mediump float;\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "uniform sampler2D inputImageTexture2;\n" +
                "varying vec2 textureCoordinate;\n" +
                "void main(){\n" +
                "  vec4 color1 = texture2D(inputImageTexture, textureCoordinate);\n" +
                "  vec4 color2 = texture2D(inputImageTexture2, textureCoordinate);\n" +
                "  float r;\n" +
                "  if (color2.r * color1.a + color1.r * color2.a >= color2.a * color1.a) {\n" +
                "    r = color2.a * color1.a + color2.r * (1.0 - color1.a) + color1.r * (1.0 - color2.a);\n" +
                "  } else {\n" +
                "    r = color2.r + color1.r;\n" +
                "  }\n" +
                "  float g;\n" +
                "  if (color2.g * color1.a + color1.g * color2.a >= color2.a * color1.a) {\n" +
                "    g = color2.a * color1.a + color2.g * (1.0 - color1.a) + color1.g * (1.0 - color2.a);\n" +
                "  } else {\n" +
                "    g = color2.g + color1.g;\n" +
                "  }\n" +
                "  float b;\n" +
                "  if (color2.b * color1.a + color1.b * color2.a >= color2.a * color1.a) {\n" +
                "    b = color2.a * color1.a + color2.b * (1.0 - color1.a) + color1.b * (1.0 - color2.a);\n" +
                "  } else {\n" +
                "    b = color2.b + color1.b;\n" +
                "  }\n" +
                "  float a  = color2.a + color1.a - color2.a * color1.a;\n" +
                "  gl_FragColor = vec4(r, g, b, a);\n" +
                "}";
    }
}
