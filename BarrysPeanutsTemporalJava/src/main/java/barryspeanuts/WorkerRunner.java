package barryspeanuts;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.lang.management.ManagementFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerRunner {
    public static void main(String[] args) {
         final Logger logger = LoggerFactory.getLogger(WorkerRunner.class);

        String TASK_QUEUE = "BarrysPeanuts";
        String hostSpecificTaskQueue = ManagementFactory.getRuntimeMXBean().getName();

        // gRPC stubs wrapper that talks to the local docker instance of temporal service.
        WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
        // client that can be used to start and signal workflows
        WorkflowClient client = WorkflowClient.newInstance(service);

        // worker factory that can be used to create workers for specific task queues
        WorkerFactory factory = WorkerFactory.newInstance(client);
        // Worker that listens on a task queue and hosts both workflow and activity implementations.
        final Worker workerForCommonTaskQueue = factory.newWorker(TASK_QUEUE);
        workerForCommonTaskQueue.registerWorkflowImplementationTypes(ShoppingCartWorkflowImpl.class);

        // Get worker to poll the host-specific task queue.
        final Worker workerForHostSpecificTaskQueue = factory.newWorker(hostSpecificTaskQueue);

        // Start all work
        factory.start();

        logger.info("Worker listening on task queue: {}.", TASK_QUEUE);
    }
}
