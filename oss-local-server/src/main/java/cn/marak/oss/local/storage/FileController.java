package cn.marak.oss.local.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.marak.oss.local.util.FileNameCounter;

/**
 * 文件上传，下载接口
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/files")
public class FileController {
	@Autowired
	private FileSystemService fileSystemService;

	@PostMapping("/upload")
	@ResponseStatus(HttpStatus.CREATED)
	public String upload(@RequestParam("file") MultipartFile file) {
		return fileSystemService.store(file);
	}

	@GetMapping("/download/{objectId}")
	public void downloadByObjectId(@PathVariable String objectId, final HttpServletResponse response) throws IOException {
		// 文件数据
		final FileData fileData = fileSystemService.loadByObjectId(objectId);
		 // 设置响应
		 response.setContentType(fileData.getContentType());
		 response.setHeader("Content-Disposition",
				 String.format("inline; filename=\"" + fileData.getName() + "\""));
		 FileCopyUtils.copy(fileData.getContent().getInputStream(), response.getOutputStream());
	}

	@GetMapping(value = "/download-zip")
	public void downloadByObjectIds(@RequestParam(name = "objectIds") List<String> objectIds,
			HttpServletResponse response) throws IOException {
		byte[] buffer = new byte[2048];

		// 查询元数据
		final List<FileData> fileDataList = this.fileSystemService.loadByObjectIds(objectIds);

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos)) {
			final FileNameCounter fileNameCounter = new FileNameCounter();
			for (final FileData fileData : fileDataList) {
				// 避免重复的文件名
				final ZipEntry zipEntry = new ZipEntry(fileNameCounter.convert(fileData.getName()));
				zos.putNextEntry(zipEntry);

				final InputStream is = fileData.getContent().getInputStream();
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1) {
					zos.write(buffer, 0, bytesRead);
				}

				zos.closeEntry();
			}
			response.setContentType("application/zip");
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Expires", "0");
			final ServletOutputStream sos = response.getOutputStream();
			sos.write(baos.toByteArray());
			sos.flush();
		}
	}

	@DeleteMapping("/{objectId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteByObjectId(@PathVariable String objectId) {
		fileSystemService.removeByObjectId(objectId);
	}
}
