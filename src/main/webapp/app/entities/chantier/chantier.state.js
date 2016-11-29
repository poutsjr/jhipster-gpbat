(function() {
    'use strict';

    angular
        .module('gpbatApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('chantier', {
            parent: 'entity',
            url: '/chantier?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.chantier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chantier/chantiers.html',
                    controller: 'ChantierController',
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
                    $translatePartialLoader.addPart('chantier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('chantier-detail', {
            parent: 'entity',
            url: '/chantier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gpbatApp.chantier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/chantier/chantier-detail.html',
                    controller: 'ChantierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('chantier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Chantier', function($stateParams, Chantier) {
                    return Chantier.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'chantier',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('chantier-detail.edit', {
            parent: 'chantier-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chantier/chantier-dialog.html',
                    controller: 'ChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chantier', function(Chantier) {
                            return Chantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chantier.new', {
            parent: 'chantier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chantier/chantier-dialog.html',
                    controller: 'ChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                reference: null,
                                adresse: null,
                                dateDebut: null,
                                dateDemandeTravaux: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('chantier', null, { reload: 'chantier' });
                }, function() {
                    $state.go('chantier');
                });
            }]
        })
        .state('chantier.edit', {
            parent: 'chantier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chantier/chantier-dialog.html',
                    controller: 'ChantierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Chantier', function(Chantier) {
                            return Chantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chantier', null, { reload: 'chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('chantier.delete', {
            parent: 'chantier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/chantier/chantier-delete-dialog.html',
                    controller: 'ChantierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Chantier', function(Chantier) {
                            return Chantier.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('chantier', null, { reload: 'chantier' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
