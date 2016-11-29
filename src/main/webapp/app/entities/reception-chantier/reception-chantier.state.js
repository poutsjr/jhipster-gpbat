(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reception-chantier', {
            parent: 'entity',
            url: '/reception-chantier?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.receptionChantier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reception-chantier/reception-chantiers.html',
                    controller: 'ReceptionChantierController',
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
                    $translatePartialLoader.addPart('receptionChantier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reception-chantier-detail', {
            parent: 'entity',
            url: '/reception-chantier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.receptionChantier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reception-chantier/reception-chantier-detail.html',
                    controller: 'ReceptionChantierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('receptionChantier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ReceptionChantier', function($stateParams, ReceptionChantier) {
                    return ReceptionChantier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reception-chantier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reception-chantier-detail.edit', {
            parent: 'reception-chantier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reception-chantier/reception-chantier-dialog.html',
                    controller: 'ReceptionChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReceptionChantier', function(ReceptionChantier) {
                            return ReceptionChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reception-chantier.new', {
            parent: 'reception-chantier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reception-chantier/reception-chantier-dialog.html',
                    controller: 'ReceptionChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateReception: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reception-chantier', null, { reload: 'reception-chantier' });
                }, function() {
                    $state.go('reception-chantier');
                });
            }]
        })
        .state('reception-chantier.edit', {
            parent: 'reception-chantier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reception-chantier/reception-chantier-dialog.html',
                    controller: 'ReceptionChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ReceptionChantier', function(ReceptionChantier) {
                            return ReceptionChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reception-chantier', null, { reload: 'reception-chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reception-chantier.delete', {
            parent: 'reception-chantier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reception-chantier/reception-chantier-delete-dialog.html',
                    controller: 'ReceptionChantierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ReceptionChantier', function(ReceptionChantier) {
                            return ReceptionChantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reception-chantier', null, { reload: 'reception-chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
