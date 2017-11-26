import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Assignment2
{
	public static BufferedImage cloneImage(BufferedImage input)
	{
		BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		for (int i = 0; i < input.getHeight(); i++)
			for (int j = 0; j < input.getWidth(); j++)
				output.setRGB(j, i, input.getRGB(j, i));
		return output;
	}

	public static void meanFilter(BufferedImage input, int kernelSize) throws IOException
	{
		BufferedImage output = cloneImage(input);
		int width = input.getWidth();
		int height = input.getHeight();
		int alpha, red, green, blue;

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				alpha = 0;
				red = 0;
				green = 0;
				blue = 0;
				int count = 0;
				for (int i = y - (kernelSize / 2); i <= y + (kernelSize / 2); i++)
					for (int j = x - (kernelSize / 2); j <= x + (kernelSize / 2); j++)
						if (i < 0 || i >= height || j < 0 || j >= width)
							continue;
						else
						{
							alpha += new Color(input.getRGB(j, i)).getAlpha();
							red += new Color(input.getRGB(j, i)).getRed();
							green += new Color(input.getRGB(j, i)).getGreen();
							blue += new Color(input.getRGB(j, i)).getBlue();
							count++;
						}

				int p = new Color(red / count, green / count, blue / count, alpha / count).getRGB();
				output.setRGB(x, y, p);
			}
		ImageIO.write(output, "png", new File("output_Mean_" + kernelSize + "x" + kernelSize + ".png"));
	}

	public static void medianFilter(BufferedImage input, int kernelSize) throws IOException
	{
		BufferedImage output = cloneImage(input);
		int width = input.getWidth();
		int height = input.getHeight();
		int red[], green[], blue[];

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				int a = new Color(input.getRGB(x, y)).getAlpha();
				red = new int[kernelSize * kernelSize];
				green = new int[kernelSize * kernelSize];
				blue = new int[kernelSize * kernelSize];
				int count = 0;
				for (int i = y - (kernelSize / 2); i <= y + (kernelSize / 2); i++)
					for (int j = x - (kernelSize / 2); j <= x + (kernelSize / 2); j++)
						if (i < 0 || i >= height || j < 0 || j >= width)
							continue;
						else
						{
							red[count] = new Color(input.getRGB(j, i)).getRed();
							green[count] = new Color(input.getRGB(j, i)).getGreen();
							blue[count] = new Color(input.getRGB(j, i)).getBlue();
							count++;
						}

				Arrays.sort(red);
				Arrays.sort(green);
				Arrays.sort(blue);
				int index = (count % 2 == 0) ? count / 2 - 1 : count / 2;
				int p = new Color(red[index], green[index], blue[index], a).getRGB();
				output.setRGB(x, y, p);
			}
		ImageIO.write(output, "png", new File("output_Median_" + kernelSize + "x" + kernelSize + ".png"));
	}

	public static void main(String[] args) throws IOException
	{
		BufferedImage input = ImageIO.read(new File("butterfly.png"));
		meanFilter(input, 3);
		meanFilter(input, 7);
		medianFilter(input, 3);
		medianFilter(input, 7);
	}
}