import javax.swing.*;
import java.awt.*;

class BorderedText extends JPanel {
  private String text;
  private Color textColor;
  private Color borderColor;
  private Color backgroundColor;

  public BorderedText(String text, Color textColor, Color borderColor, Color backgroundColor) {
    this.text = text;
    this.textColor = textColor;
    this.borderColor = borderColor;
    this.backgroundColor = backgroundColor;
    setPreferredSize(new Dimension(100, 80)); // Adjusted size
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // 绘制背景颜色
    g.setColor(backgroundColor);
    g.fillRect(0, 0, getWidth(), getHeight());

    // 绘制文本外边框
    g.setColor(borderColor);
    g.setFont(getFont());
    FontMetrics fm = g.getFontMetrics();
    int textWidth = fm.stringWidth(text);
    int textHeight = fm.getHeight();
    int x = (getWidth() - textWidth) / 2;
    int y = (getHeight() - textHeight) / 2 + fm.getAscent();
    g.drawString(text, x - 1, y); // 左偏移
    g.drawString(text, x + 1, y); // 右偏移
    g.drawString(text, x, y - 1); // 上偏移
    g.drawString(text, x, y + 1); // 下偏移

    // 绘制文本
    g.setColor(textColor);
    g.drawString(text, x, y);
  }

  public void setText(String text) {
    this.text = text;
    repaint();
  }
}
