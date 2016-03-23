package com.taksila.veda.course.slides;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */

import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.transcoder.wmf.tosvg.WMFPainter;
import org.apache.batik.transcoder.wmf.tosvg.WMFRecordStore;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFComments;
import org.apache.poi.xslf.usermodel.XSLFImageRenderer;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFRenderingHint;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import com.taksila.veda.model.api.course.v1_0.CreateSlideRequest;
import com.taksila.veda.model.api.course.v1_0.CreateSlideResponse;
import com.taksila.veda.model.api.course.v1_0.GetSlideRequest;
import com.taksila.veda.model.api.course.v1_0.GetSlideResponse;
import com.taksila.veda.model.api.course.v1_0.Slide;
import com.taksila.veda.model.api.course.v1_0.UpdateSlideResponse;
import com.taksila.veda.utils.CommonUtils;

/**
 * Convert each slide of a .pptx presentation into SVG
 *
 * @author Yegor Kozlov
 */
public class Pptx2Image 
{
	private static String baseDirectory = "C:\\files\\"; 
	static Logger logger = LogManager.getLogger(SlideComponent.class.getName());	
	static class Pptx2ImageOptions
	{
		double scale = 1;
		String filename = "";
		String format = "png";
		public int topicid;		
		
		
	}
	
//    public static void convertToSvg(String filename) throws Exception 
//    {
//         System.out.println("Processing " + filename);
//
//        // read the .pptx file
////        File f = new File(filename);
////        InputStream st = new FileInputStream(f);
//        XMLSlideShow ppt = new XMLSlideShow(OPCPackage.open("C:\\files\\"+filename));
////        XMLSlideShow ppt = new XMLSlideShow(OPCPackage.open(f));
//
//        Dimension pgsize = ppt.getPageSize();
//
//        // convert each slide into a .svg file
//        List<XSLFSlide> slides = ppt.getSlides();
//        int i = 0;
//		for (XSLFSlide slide: slides) 
//        {
//            i++;
//			// Create initial SVG DOM
//            DOMImplementation domImpl = SVGDOMImplementation.getDOMImplementation();
//            Document doc = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);
//            /*
//             * Use Batik SVG Graphics2D driver
//             */
//            SVGGraphics2D graphics = new SVGGraphics2D(doc);
//            graphics.setRenderingHint(XSLFRenderingHint.IMAGE_RENDERER, new WMFImageRender());
//            graphics.setSVGCanvasSize(pgsize);
//
//            String title = slide.getTitle();
//            System.out.println("Rendering slide " + (i) + (title == null ? "" : ": " + title));
//
//            /*
//             *  draw stuff. All the heavy-lifting happens here
//             */
//            slide.draw(graphics);
//
//            /*
//             *  save the result.
//             */
//            int sep = filename.lastIndexOf(".");
//            String fname = filename.substring(0, sep == -1 ? filename.length() : sep) + "-" + (i + 1) + ".svg";
//            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("C:\\files\\svg\\"+fname), "UTF-8");
//            DOMSource domSource = new DOMSource(graphics.getRoot());
//            StreamResult streamResult = new StreamResult(out);
//            TransformerFactory tf = TransformerFactory.newInstance();
//            Transformer serializer = tf.newTransformer();
//            serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//            serializer.transform(domSource, streamResult);
//            out.flush();
//            out.close();
//        }
//        System.out.println("Done");
//    }

    
    public static String convertToImage(Pptx2ImageOptions options) throws IOException, Exception 
    {
    	int i = 0;    
    	System.out.println("Processing " + options.filename +" and topicid "+options.topicid);
    	String tempFolderId = "";
     	
    	if (StringUtils.isBlank(options.filename)) 
    	{
    		throw new Exception("File not specified or it doesn't exist");
        }

        if (StringUtils.isBlank(options.filename)) 
        {
        	throw new Exception("File not specified or it doesn't exist");
        }
        
        if (options.topicid <= 0) 
        {
        	throw new Exception("Topic id not specified");
        }
         
//         if (StringUtils.isBlank(options.filename) outdir == null || !outdir.exists() || !outdir.isDirectory()) 
//         {
//             usage("Output directory doesn't exist");
//             return;
//         }

         if (options.scale < 0) 
         {
        	 throw new Exception("File scale is not valid please provide a valide value between 0 - 1");             
         }
         
         if (options.format == null || !options.format.matches("^(png|gif|jpg|null)$")) 
         {
        	 throw new Exception("Output format is not valid. Should be either png, gif or jpg ");             
         }
    	
//    	String format = "png";

         // read the .pptx file
//         File f = new File(filename);
//         InputStream st = new FileInputStream(f);
         
         XMLSlideShow ppt = new XMLSlideShow(OPCPackage.open(baseDirectory+"\\upload\\"+options.filename));
//         XMLSlideShow ppt = new XMLSlideShow(OPCPackage.open(f));

         Dimension pgsize = ppt.getPageSize();
    	int width = (int) (pgsize.width * options.scale);
        int height = (int) (pgsize.height * options.scale);
        List<XSLFSlide> slides = ppt.getSlides();
  		for (XSLFSlide slide: slides) 
        {
  			i++;	    	
	        String title = slide.getTitle();
	        String desc = "";
	        XSLFComments slideComments = slide.getComments();
	        if (slideComments != null && slideComments.getNumberOfComments() > 0)
	        {
	        	for (int c=0;c<slideComments.getNumberOfComments() ;c++)
	        		desc += slideComments.getCommentAt(c);
	        }
	        System.out.println("Rendering slide " + i + (title == null ? "" : ": " + title));
	        
	         BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	         Graphics2D graphics = img.createGraphics();
//	         DrawFactory.getInstance(graphics)).fixFonts(graphics);
	     
	         // default rendering options
	         graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	         graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	         graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	         graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	
	         graphics.scale(options.scale, options.scale);
	
	         // draw stuff
	         slide.draw(graphics);
	
	         // save the result
	         if (!"null".equals(options.format)) 
	         {
	             String outname = options.filename.replaceFirst(".pptx?", "");
	             outname = String.format(Locale.ROOT, "%1$s-%2$04d.%3$s", outname, i, options.format);
	             
	             String outFilePath = "C:\\files\\"+options.format+"\\";
	             if (options.scale < 1)
	            	 outFilePath = outFilePath+"thumbs\\";
	             
	             File outfile = new File(outFilePath, outname);
	             ByteArrayOutputStream os = new ByteArrayOutputStream();
//	             ImageIO.write(img, options.format, outfile);
	             ImageIO.write(img, options.format, os);
	             InputStream imgis = new ByteArrayInputStream(os.toByteArray());
	             
	             /*
	              * write it into db
	              */
	             SlideComponent slideComp = new SlideComponent("1");
	             	             
	             GetSlideRequest req = new GetSlideRequest();
	             req.setName(outname);
	             GetSlideResponse slideResp = slideComp.getSlideByName(req);
	             Slide targetSlide = slideResp.getSlide();
	             
	             if (targetSlide == null)
	             {
		             CreateSlideRequest newSlideReq = new CreateSlideRequest();
		             targetSlide = new Slide();
		             newSlideReq.setSlide(targetSlide);
		             targetSlide.setDescription(desc);
		             targetSlide.setTitle(title);
		             targetSlide.setName(outname);
		             targetSlide.setTopicid(options.topicid);
		             CreateSlideResponse newSlideResp = slideComp.createNewSlide(newSlideReq);
		             targetSlide = newSlideResp.getSlide();
	             }
	             
	             if (targetSlide != null)
	             {
	            	 UpdateSlideResponse imgUptResp = slideComp.updateSlideImage(targetSlide.getId(), imgis, options.format,options.scale);
	            	 if (!imgUptResp.isSuccess())
	            	 {
	            		 logger.trace(CommonUtils.toJson(imgUptResp));
	            	 }
	             }
	             else
	             {
	            	 logger.trace(" FAILED TO LOAD THE IMAGE for slide =  "+outname);
	             }
	            	 
	            
	             
	             
	         }
        }
  		
  		return tempFolderId;
    }
    
    
    /**
     * Image renderer with support for .wmf images
     */
    static class WMFImageRender extends XSLFImageRenderer 
    {

        /**
         * Use Apache Batik to render WMF,
         * delegate all other types of images to the javax.imageio framework
       */
        @Override
        public boolean drawImage(Graphics2D graphics, XSLFPictureData data,Rectangle2D anchor) 
        {
            try 
            {
                // see what type of image we are
                PackagePart part = data.getPackagePart();
                String contentType = part.getContentType();
                if (contentType.equals("image/x-wmf")) 
                {
                    WMFRecordStore currentStore = new WMFRecordStore();
                    currentStore.read(new DataInputStream(part.getInputStream()));
                    int wmfwidth = currentStore.getWidthPixels();
                    float conv = (float) anchor.getWidth() / wmfwidth;

                    // Build a painter for the RecordStore
                    WMFPainter painter = new WMFPainter(currentStore,
                            (int) anchor.getX(), (int) anchor.getY(), conv);
                    painter.paint(graphics);
                } 
                else 
                {
                    BufferedImage img = ImageIO.read(data.getPackagePart().getInputStream());
                    graphics.drawImage(img,
                            (int) anchor.getX(), (int) anchor.getY(),
                            (int) anchor.getWidth(), (int) anchor.getHeight(), null);
                }
            } 
            catch (Exception e) 
            {
                return false;
            }
            return true;
        }

        /**
         * Convert data form the supplied package part into a BufferedImage.
         * This method is used to create texture paint.
         */
        @Override
        public BufferedImage readImage(PackagePart packagePart) throws IOException {
            String contentType = packagePart.getContentType();
            if (contentType.equals("image/x-wmf")) {
                try {
                    WMFRecordStore currentStore = new WMFRecordStore();
                    currentStore.read(new DataInputStream(packagePart.getInputStream()));
                    int wmfwidth = currentStore.getWidthPixels();
                    int wmfheight = currentStore.getHeightPixels();

                    BufferedImage img = new BufferedImage(wmfwidth, wmfheight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = img.createGraphics();

                    // Build a painter for the RecordStore
                    WMFPainter painter = new WMFPainter(currentStore, 0, 0, 1.0f);
                    painter.paint(graphics);

                    return img;
                } catch (IOException e) {
                    return null;
                }
            } else {
                return ImageIO.read(packagePart.getInputStream());
            }
        }

    }
}