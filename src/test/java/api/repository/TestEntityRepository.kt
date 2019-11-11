package api.repository

import api.entity.TestEntity
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource
interface TestEntityRepository : TestBaseRepository<TestEntity>