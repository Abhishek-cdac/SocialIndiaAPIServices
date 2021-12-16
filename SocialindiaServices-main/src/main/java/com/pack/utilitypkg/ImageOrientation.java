package com.pack.utilitypkg;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.formats.jpeg.JpegImageMetadata;
import org.apache.sanselan.formats.jpeg.exifRewrite.ExifRewriter;
import org.apache.sanselan.formats.tiff.TiffImageMetadata;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;

public class ImageOrientation {
	
	public static BufferedImage getBufferImgOrientation(String lPimgSourcePath,String lPimageFomat) {
		// TODO Auto-generated method stub
		BufferedImage originalImage = null;
		InputStream instream = null;
		try {
			System.out.println("Enter ImageOrientation--------------------------");
			instream = new FileInputStream(lPimgSourcePath);
        	byte[] imageData  = IOUtils.toByteArray(instream);
        	//System.out.println("imageData arrval--------------------------" + imageData);
        	originalImage = ImageIO.read(new ByteArrayInputStream(imageData));    
        	//System.out.println("originalImage value--------------------------" + originalImage);
        	if (originalImage != null) {
            IImageMetadata imageMetadata = Sanselan.getMetadata(imageData);
            if (imageMetadata != null) {
            //System.out.println("imageMetadata value--------------------------" + imageMetadata);
            JpegImageMetadata jpegMetadata = (JpegImageMetadata)imageMetadata;
            TiffImageMetadata metadata = jpegMetadata.getExif();
            if (metadata != null) {
            System.out.println("metadata value--------------------------" + metadata);
            imageData = null;
            int orientation = -1;
			//if ( lPimageFomat.equalsIgnoreCase("jpg") || lPimageFomat.equalsIgnoreCase("jpeg")) {
				System.out.println("Enter orientation source code------------");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write( originalImage, lPimageFomat, baos );
				baos.flush();
				byte[] imageInByte = baos.toByteArray();
				baos.close();
				 if (metadata != null) {
		            	ByteArrayOutputStream out = new ByteArrayOutputStream();
		                new ExifRewriter().updateExifMetadataLossless(imageInByte, out, metadata.getOutputSet());
		                out.close(); 
		                imageData = out.toByteArray();
		                //System.out.println("Bytearr imageData--------------------------" + imageData);
		            }
				ByteArrayInputStream is = new ByteArrayInputStream( imageData );
				orientation = getRotation(is);
				System.out.println("orientation-----------------------------" + orientation);
			//}
			
			switch (orientation) {
            case 1:
                break;
            case 2: // Flip X
            	originalImage = Scalr.rotate(originalImage, Rotation.FLIP_HORZ);
                break;
            case 3: // PI rotation
            	originalImage = Scalr.rotate(originalImage, Rotation.CW_180);
                break;
            case 4: // Flip Y
            	originalImage = Scalr.rotate(originalImage, Rotation.FLIP_VERT);
                break;
            case 5: // - PI/2 and Flip X
            	originalImage = Scalr.rotate(originalImage, Rotation.CW_90);
            	originalImage = Scalr.rotate(originalImage, Rotation.FLIP_HORZ);
                break;
            case 6: // -PI/2 and -width
            	originalImage = Scalr.rotate(originalImage, Rotation.CW_90);
            	//originalImage = Scalr.rotate(originalImage, Rotation.FLIP_VERT);
                break;
            case 7: // PI/2 and Flip
            	originalImage = Scalr.rotate(originalImage, Rotation.CW_90);
            	originalImage = Scalr.rotate(originalImage, Rotation.FLIP_VERT);
                break;
            case 8: // PI / 2
            	originalImage = Scalr.rotate(originalImage, Rotation.CW_270);
                break;
            default:
                break;
            }  
            } else {
        		return originalImage;
        	}
            // ---- End orientation and Metadata handling ----
        	} else {
        		return originalImage;
        	}
			
		} else {
			originalImage = null;
		}
			
		} catch (Exception ex) {
            System.out.println("Exception found in ImageCompress.java : " + ex + "--line number-- " + new Exception().getStackTrace()[0].getLineNumber());         
        } finally {
            instream = null;
        }
		return originalImage;
	}
    
    public static int getRotation(InputStream in) throws IOException {
		int [] exif_data = new int[100];
		int n_flag = 0, set_flag = 0;
		int is_motorola = 0;
		
		/* Read File head, check for JPEG SOI + Exif APP1 */
		for (int i = 0; i < 4; i++)
			exif_data[i] = readByte(in);
		
		if (exif_data[0] != 0xFF || exif_data[1] != 0xD8 || exif_data[2] != 0xFF || exif_data[3] != 0xE1)
			return -2;

		/* Get the marker parameter length count */
		int length = read2bytes(in);
		// exif_data = new int[length];

		/* Length includes itself, so must be at least 2 */
		/* Following Exif data length must be at least 6 */
		if (length < 8)
			return -1;
		length -= 8;
		/* Read Exif head, check for "Exif" */
		for (int i = 0; i < 6; i++)
			exif_data[i] = in.read();
		
		if (exif_data[0] != 0x45 || exif_data[1] != 0x78 || exif_data[2] != 0x69 || exif_data[3] != 0x66 || exif_data[4] != 0 || exif_data[5] != 0)
			return -1;
		
		/* Read Exif body */
		length = length > exif_data.length ? exif_data.length : length;
		for (int i = 0; i < length; i++)
			exif_data[i] = in.read();

		if (length < 12)
			return -1; /* Length of an IFD entry */

		/* Discover byte order */
		if (exif_data[0] == 0x49 && exif_data[1] == 0x49)
			is_motorola = 0;
		else if (exif_data[0] == 0x4D && exif_data[1] == 0x4D)
			is_motorola = 1;
		else
			return -1;

		/* Check Tag Mark */
		if (is_motorola == 1) {
			if (exif_data[2] != 0)
				return -1;
			if (exif_data[3] != 0x2A)
				return -1;
		} else {
			if (exif_data[3] != 0)
				return -1;
			if (exif_data[2] != 0x2A)
				return -1;
		}

		/* Get first IFD offset (offset to IFD0) */
		int offset;
		if (is_motorola == 1) {
			if (exif_data[4] != 0)
				return -1;
			if (exif_data[5] != 0)
				return -1;
			offset = exif_data[6];
			offset <<= 8;
			offset += exif_data[7];
		} else {
			if (exif_data[7] != 0)
				return -1;
			if (exif_data[6] != 0)
				return -1;
			offset = exif_data[5];
			offset <<= 8;
			offset += exif_data[4];
		}
		if (offset > length - 2)
			return -1; /* check end of data segment */

		/* Get the number of directory entries contained in this IFD */
		int number_of_tags;
		if (is_motorola == 1) {
			number_of_tags = exif_data[offset];
			number_of_tags <<= 8;
			number_of_tags += exif_data[offset + 1];
		} else {
			number_of_tags = exif_data[offset + 1];
			number_of_tags <<= 8;
			number_of_tags += exif_data[offset];
		}
		if (number_of_tags == 0)
			return -1;
		offset += 2;

		/* Search for Orientation Tag in IFD0 */
		for (;;) {
			if (offset > length - 12)
				return -1; /* check end of data segment */
			/* Get Tag number */
			int tagnum;
			if (is_motorola == 1) {
				tagnum = exif_data[offset];
				tagnum <<= 8;
				tagnum += exif_data[offset + 1];
			} else {
				tagnum = exif_data[offset + 1];
				tagnum <<= 8;
				tagnum += exif_data[offset];
			}
			if (tagnum == 0x0112)
				break; /* found Orientation Tag */
			if (--number_of_tags == 0)
				return -1;
			offset += 12;
		}

		/*
		 * if (set_flag==1) { Set the Orientation value if (is_motorola==1) {
		 * exif_data[offset+2] = 0; Format = unsigned short (2 octets)
		 * exif_data[offset+3] = 3; exif_data[offset+4] = 0; Number Of
		 * Components = 1 exif_data[offset+5] = 0; exif_data[offset+6] = 0;
		 * exif_data[offset+7] = 1; exif_data[offset+8] = 0; exif_data[offset+9]
		 * = set_flag; exif_data[offset+10] = 0; exif_data[offset+11] = 0; }
		 * else { exif_data[offset+2] = 3; Format = unsigned short (2 octets)
		 * exif_data[offset+3] = 0; exif_data[offset+4] = 1; Number Of
		 * Components = 1 exif_data[offset+5] = 0; exif_data[offset+6] = 0;
		 * exif_data[offset+7] = 0; exif_data[offset+8] = set_flag;
		 * exif_data[offset+9] = 0; exif_data[offset+10] = 0;
		 * exif_data[offset+11] = 0; } }
		 */
		// else {
		/* Get the Orientation value */
		if (is_motorola == 1) {
			if (exif_data[offset + 8] != 0)
				return -1;
			set_flag = exif_data[offset + 9];
		} else {
			if (exif_data[offset + 9] != 0)
				return -1;
			set_flag = exif_data[offset + 8];
		}
		if (set_flag > 8)
			return -1;
		// }

		/* Write out Orientation value */

		if (n_flag == 1)
			System.out.println("set_flag " + set_flag);
		else
			System.out.println("set_flag " + set_flag);

		return set_flag;
	}
    
    private final static int read2bytes(InputStream in) throws IOException  {
		return in.read() << 8 | in.read();
	}

	private final static int readByte(InputStream in) throws IOException {
		return in.read();
	}

	
}
