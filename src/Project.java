import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Project
{
	public static BufferedImage cloneImage(BufferedImage input)
	{
		BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(), input.getType());
		for (int i = 0; i < input.getHeight(); i++)
			for (int j = 0; j < input.getWidth(); j++)
				output.setRGB(j, i, input.getRGB(j, i));
		return output;
	}

	public static void autoThreshold(BufferedImage input) throws IOException
	{
		BufferedImage img = cloneImage(input);
		int thresholdValue = 127, iThreshold;
		int sum1, sum2, count1, count2;
		int mean1, mean2;

		while (true)
		{
			sum1 = sum2 = count1 = count2 = 0;
			for (int y = 0; y < img.getHeight(); y++)
				for (int x = 0; x < img.getWidth(); x++)
				{
					int r = new Color(img.getRGB(x, y)).getRed();
					int g = new Color(img.getRGB(x, y)).getGreen();
					int b = new Color(img.getRGB(x, y)).getBlue();
					int avgOfRGB = (r + g + b) / 3;

					if (avgOfRGB < thresholdValue)
					{
						sum1 += avgOfRGB;
						count1++;
					}
					else
					{
						sum2 += avgOfRGB;
						count2++;
					}
				}

			mean1 = (count1 > 0) ? (int) (sum1 / count1) : 0;
			mean2 = (count2 > 0) ? (int) (sum2 / count2) : 0;

			iThreshold = (mean1 + mean2) / 2;

			if (thresholdValue != iThreshold)
				thresholdValue = iThreshold;
			else
				break;
		}

		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++)
			{
				int r = new Color(img.getRGB(x, y)).getRed();
				int g = new Color(img.getRGB(x, y)).getGreen();
				int b = new Color(img.getRGB(x, y)).getBlue();
				int avgOfRGB = (r + g + b) / 3;

				if (avgOfRGB >= thresholdValue)
					img.setRGB(x, y, new Color(255, 255, 255, 255).getRGB());
				else
					img.setRGB(x, y, new Color(0, 0, 0, 255).getRGB());

			}
		ImageIO.write(img, "gif", new File("Binary_I.gif"));
	}

	public static void separateBackground(BufferedImage input) throws IOException
	{
		BufferedImage img = cloneImage(input);
		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++)
				if (img.getRGB(x, y) == Color.WHITE.getRGB())
					img.setRGB(x, y, new Color(0,0,0).getRGB());
		ImageIO.write(img, "gif", new File("BG_I.gif"));
	}

	public static void main(String[] args) throws IOException
	{
		autoThreshold(ImageIO.read(new File("sat_noisy.gif")));
		separateBackground(ImageIO.read(new File("Binary_I.gif")));

	}
}