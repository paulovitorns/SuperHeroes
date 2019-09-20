package br.com.superheroes.library.reactivex

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun workerThread(): Scheduler
    fun postWorkerThread(): Scheduler
}
