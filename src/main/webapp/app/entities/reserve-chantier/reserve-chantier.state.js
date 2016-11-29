(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reserve-chantier', {
            parent: 'entity',
            url: '/reserve-chantier?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.reserveChantier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reserve-chantier/reserve-chantiers.html',
                    controller: 'ReserveChantierController',
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
                    $translatePartialLoader.addPart('reserveChantier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reserve-chantier-detail', {
            parent: 'entity',
            url: '/reserve-chantier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.reserveChantier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reserve-chantier/reserve-chantier-detail.html',
                    controller: 'ReserveChantierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reserveChantier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ReserveChantier', function($stateParams, ReserveChantier) {
                    return ReserveChantier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reserve-chantier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reserve-chantier-detail.edit', {
            parent: 'reserve-chantier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reserve-chantier/reserve-chantier-dialog.html',
                    controller: 'ReserveChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReserveChantier', function(ReserveChantier) {
                            return ReserveChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reserve-chantier.new', {
            parent: 'reserve-chantier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reserve-chantier/reserve-chantier-dialog.html',
                    controller: 'ReserveChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                reserves: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reserve-chantier', null, { reload: 'reserve-chantier' });
                }, function() {
                    $state.go('reserve-chantier');
                });
            }]
        })
        .state('reserve-chantier.edit', {
            parent: 'reserve-chantier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reserve-chantier/reserve-chantier-dialog.html',
                    controller: 'ReserveChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReserveChantier', function(ReserveChantier) {
                            return ReserveChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reserve-chantier', null, { reload: 'reserve-chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reserve-chantier.delete', {
            parent: 'reserve-chantier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reserve-chantier/reserve-chantier-delete-dialog.html',
                    controller: 'ReserveChantierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReserveChantier', function(ReserveChantier) {
                            return ReserveChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reserve-chantier', null, { reload: 'reserve-chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
