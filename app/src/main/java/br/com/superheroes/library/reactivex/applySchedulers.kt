package br.com.superheroes.library.reactivex

import io.reactivex.CompletableTransformer
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Single
import io.reactivex.SingleTransformer

/**
 * Applies the schedulers to a single source without change the upstream Single.
 * @param schedulerProvider the SchedulerProvider instance that contains the workerThread and the postWorkerThread
 * @return the transformed SingleSource instance
 */
fun <T> applySingleSchedulers(schedulerProvider: SchedulerProvider): SingleTransformer<T, T> {
    return SingleTransformer { observer ->
        observer.flatMap { Single.just(it) }
            .subscribeOn(schedulerProvider.workerThread())
            .observeOn(schedulerProvider.postWorkerThread())
    }
}

fun <T> applyFlowableSchedulers(schedulerProvider: SchedulerProvider): FlowableTransformer<T, T> {
    return FlowableTransformer { observer ->
        observer.flatMap { Flowable.just(it) }
            .subscribeOn(schedulerProvider.workerThread())
            .observeOn(schedulerProvider.postWorkerThread())
    }
}

fun applyCompletableSchedulers(schedulerProvider: SchedulerProvider): CompletableTransformer {
    return CompletableTransformer { observer ->
        observer
            .subscribeOn(schedulerProvider.workerThread())
            .observeOn(schedulerProvider.postWorkerThread())
    }
}
