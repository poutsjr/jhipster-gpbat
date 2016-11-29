(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('simulation', {
            parent: 'entity',
            url: '/simulation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.simulation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/simulation/simulations.html',
                    controller: 'SimulationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('simulation');
                    $translatePartialLoader.addPart('etatSimulation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('simulation-detail', {
            parent: 'entity',
            url: '/simulation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.simulation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/simulation/simulation-detail.html',
                    controller: 'SimulationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('simulation');
                    $translatePartialLoader.addPart('etatSimulation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Simulation', function($stateParams, Simulation) {
                    return Simulation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'simulation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('simulation-detail.edit', {
            parent: 'simulation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/simulation/simulation-dialog.html',
                    controller: 'SimulationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Simulation', function(Simulation) {
                            return Simulation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('simulation.new', {
            parent: 'simulation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/simulation/simulation-dialog.html',
                    controller: 'SimulationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateSimulation: null,
                                remarques: null,
                                etat: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('simulation', null, { reload: 'simulation' });
                }, function() {
                    $state.go('simulation');
                });
            }]
        })
        .state('simulation.edit', {
            parent: 'simulation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/simulation/simulation-dialog.html',
                    controller: 'SimulationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Simulation', function(Simulation) {
                            return Simulation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('simulation', null, { reload: 'simulation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('simulation.delete', {
            parent: 'simulation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/simulation/simulation-delete-dialog.html',
                    controller: 'SimulationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Simulation', function(Simulation) {
                            return Simulation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('simulation', null, { reload: 'simulation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
