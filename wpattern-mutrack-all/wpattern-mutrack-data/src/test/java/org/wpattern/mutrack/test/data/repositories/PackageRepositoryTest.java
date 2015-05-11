package org.wpattern.mutrack.test.data.repositories;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.wpattern.mutrack.data.repositories.IPackageRepository;
import org.wpattern.mutrack.test.data.utils.AbstractDataTest;
import org.wpattern.mutrack.utils.entities.PackageEntity;

public class PackageRepositoryTest extends AbstractDataTest {

	private final Logger LOGGER = Logger.getLogger(this.getClass());

	@Inject
	private IPackageRepository packageRepository;

	@Test
	public void testFindAll() {
		List<PackageEntity> packages = this.packageRepository.findAll();

		if (this.LOGGER.isDebugEnabled()) {
			this.LOGGER.debug(packages);
		}
	}

}
